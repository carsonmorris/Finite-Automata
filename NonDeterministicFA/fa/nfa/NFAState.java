package fa.nfa;

import java.util.*;

/**
 * The state class for the NFA that extends fa.State
 * @authors Carson M and Joseph C
 */
public class NFAState extends fa.State{
    
    //NFAState transitions 
	private LinkedHashMap<Character, Set<NFAState>> transitions;
	//Boolean for if a state is a final state
    private boolean isFinal;
    
    
    /**
     * Default Constructor
     * @param name - the name of the state
     */
    public NFAState(String name){
        this.name = name;
        transitions = new LinkedHashMap<Character, Set<NFAState>>();
        isFinal = false;
	}

    /**
     * Constructor for creating a state thats a final state at creation
     * @param name - the name of the state
     * @param isFinal - boolean for indicating if the state is final, ideally to create a final state without having to set it separately.
     */
    public NFAState(String name, boolean isFinal){
    	this.name = name;
        transitions = new LinkedHashMap<Character, Set<NFAState>>();
        this.isFinal = isFinal;
    }


     /**
     * Adds a transition for the state
     * @param symbToTrans - the symbol that the transition will occur on
     * @param nextState - the state that will be transitioned to on that symbol
     */
    public void addTransition(char symbToTrans, NFAState nextState){
        if(transitions.containsKey(symbToTrans)){
            transitions.get(symbToTrans).add(nextState);

        }else{
            Set<NFAState> temp = new LinkedHashSet<>();
            temp.add(nextState);
            transitions.put(symbToTrans, temp);
        }
    }

    /**
     * Gets the set of transition states for a given symbol
     * 
     * @param symbToTrans - the symbol that the transition(s) occur on
     * @return The set of states that the provided symbol can transition to
     */
    public Set<NFAState> getTo(char symbToTrans){
        //Look for the set of transition states
        if(transitions.containsKey(symbToTrans)){
            return transitions.get(symbToTrans);
        
        }else{
            //If one is not found, return an empty set
            return new LinkedHashSet<>();
        }
    }
    
    /**
     * Used to check if a state is a final state
     * @return - whether the state is a final state
     */
    public boolean isFinal(){
        return isFinal;
    }


}
