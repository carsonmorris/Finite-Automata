package fa.nfa;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import fa.State;
import fa.dfa.DFA;

/**
 * The NFA class that implements fa.nfa.NFAInterface interface
 * @authors Carson M and Joseph C
 */
public class NFA implements fa.nfa.NFAInterface {

    private NFAState startState;
    private LinkedHashSet<Character> characterSet;
    private LinkedHashSet<NFAState> NFAStateSet;

    /**
     * Default constructor
     */
    public NFA() {
        NFAStateSet = new LinkedHashSet<NFAState>();
        characterSet = new LinkedHashSet<Character>();
    }

    @Override
    public void addStartState(String name) {
       
        NFAState currState = getState(name);

        //If the current state does not exist (is null)
        if(currState == null) {
            currState = new NFAState(name);
            NFAStateSet.add(currState);
        }

       startState = currState;
    }

    @Override
    public void addState(String name) {
        NFAStateSet.add(new NFAState(name));
    }

    /**
     *  Gets a state if it exists
     * @param name - the name of the state
     * @return - the state we want, null if it doesnt exist
     */
    private NFAState getState(String name) {
        NFAState currState = null;

        for(NFAState state : NFAStateSet){
            if(state.getName().equals(name)){
                currState = state;
                break;
            }
        }
        return currState;
    }

    @Override
    public void addFinalState(String name) {
        
        NFAState state = new NFAState(name, true); 
        NFAStateSet.add(state);
    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
        (getState(fromState)).addTransition(onSymb, getState(toState));

        if(onSymb != 'e' && !characterSet.contains(onSymb)) {
            characterSet.add(onSymb);
        }    
    }

    @Override
    public LinkedHashSet<NFAState> getStates() {
        return NFAStateSet;
    }

    @Override
    public Set<? extends State> getFinalStates() {

        LinkedHashSet<NFAState> finalStateState = new LinkedHashSet<>();
        for(NFAState state : NFAStateSet){
            if (state.isFinal()) {
                finalStateState.add(state);
            }
        }

        return finalStateState;
    }

    /**
     * Looks through the set of states and returns true if a final state is in the set
     * @param states - the set of all states
     * @return - True if the set contains a final state
     */
    private boolean containsFinalState(Set<NFAState> states) {
        for(NFAState state:states){
            if(state.isFinal()){
                return true;
            }
        }

        return false;
    }

    @Override
    public State getStartState() {
        return startState;
    }

    @Override
    public Set<Character> getABC() {
        return characterSet;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    @Override
    public Set<NFAState> eClosure(NFAState state) {
    	LinkedHashSet<NFAState> list = new LinkedHashSet<>();
    	return depthFirstSearch(list, state);
    }

    /**
     * A depth first search for all the accessible states from a given state on an epsilon transition
     * @param visited - Set of visited states
     * @param state - state we are examining
     * @return - the set of eClosures
     */
    private Set<NFAState> depthFirstSearch(LinkedHashSet<NFAState> visited, NFAState state) {
        LinkedHashSet<NFAState> statesVisited = visited;
    	LinkedHashSet<NFAState> eClosureSets = new LinkedHashSet<>();
    
        //Add the NFA state to the eClosureSet
        eClosureSets.add(state);
       
        if(!state.getTo('e').isEmpty() && !statesVisited.contains(state)){
            statesVisited.add(state);
            for(NFAState nfaState : state.getTo('e')){
                eClosureSets.addAll(depthFirstSearch(statesVisited, nfaState));
            }
        }
        return eClosureSets;
    }

    @Override
    public DFA getDFA() {
        //Create a new DFA
        DFA dfa = new DFA();
        
        //Set up lists, sets, maps
        Map<Set<NFAState>, String> visited = new LinkedHashMap<>();
        Set<NFAState> states = eClosure(startState);
        
        // Add to visited set 
        visited.put(states, states.toString());
        
        LinkedList<Set<NFAState>> stateTracker = new LinkedList<>();
        stateTracker.add(states);

        //Add the start state
        dfa.addStartState(visited.get(states));

        //Work through the stateTracker
        while(!stateTracker.isEmpty()){
            states = stateTracker.poll();

            for (char c : characterSet) {
            	LinkedHashSet<NFAState> temp = new LinkedHashSet<>();

                for (NFAState state : states) {
                    temp.addAll(state.getTo(c));
                }

                LinkedHashSet<NFAState> temp1 = new LinkedHashSet<>();

                for(NFAState state : temp){
                    temp1.addAll(eClosure(state));
                }
                if(!visited.containsKey(temp1)){

                    visited.put(temp1, temp1.toString());
                    stateTracker.add(temp1);

                    if(containsFinalState(temp1)){
                        dfa.addFinalState(visited.get(temp1));

                    }else{
                        dfa.addState(visited.get(temp1));

                    }
                }
                // Add transitions to the DFA 
                dfa.addTransition(visited.get(states), c, visited.get(temp1));
            }
        }
        return dfa;
    }

}
