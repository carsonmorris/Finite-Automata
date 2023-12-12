/**
 * @Authors Carson Morris and Joseph Cierzan
 * CS 361
 * Implement the methods in DFAInterface and FAInterface
 */

package fa.dfa;

import fa.State;
import java.util.Set;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class DFA implements DFAInterface {

    // The set of states
    private final Set<DFAState> setOfStates;
    // The alphabet
    private final Set<Character> alphabet;
    // The start state
    private DFAState startState;
    // The set of final states
    private final Set<DFAState> finalStates;
    // The transition table
    private final HashMap<HashMap<DFAState, Character>, DFAState> transitionTable;
    

    /**
     * Default constructor.
     */
    public DFA() {
        // Initialize the instance variables
        setOfStates = new LinkedHashSet<>();
        alphabet = new LinkedHashSet<Character>();
        startState = null;
        finalStates = new LinkedHashSet<>();
        transitionTable = new HashMap<HashMap<DFAState, Character>, DFAState>();
    }

    @Override
    public boolean accepts(String s) {
        // Get our string we are parsing and grab our startState
        char[] stringToParse = s.toCharArray();
        DFAState currentState = startState;
        
        // For each transition in the string to parse...
        for (char transition : stringToParse) {
            
            // If the transition isn't in the alphabet, return false - not accepted
            if (!alphabet.contains(transition)) {
                return false;
            }
            // Move to the next state
            currentState = (DFAState) getToState(currentState, transition);
        }
        // Once the loop is done, check if we are in a final state, if true - accepted
        if (currentState.isFinalState()) {
            return true;
        }
        // If none of the steps taken above work, return false - not accepted
        return false;
    }

    @Override
    public State getToState(DFAState from, char onSymb) {
        // Create a hashmap and use our given parameters
        HashMap<DFAState, Character> hMap = new HashMap<DFAState, Character>();
        hMap.put(from, onSymb);

        // Use our transition table to get our next state
        DFAState toState = transitionTable.get(hMap);

        // Return our next state
        return toState;
    }

    @Override
    public void addStartState(String name) {
        boolean alreadyExists = false;
        // Check if our start state already exists, and if so make it the start state
        for (DFAState state : setOfStates) {
            if (state.toString().compareTo(name) == 0) {
                startState = state;
                alreadyExists = true;
            }
        }
        // If we didn't find the start state in our existing states, create it and add
        // it to the the set of states
        if (!alreadyExists) {
            startState = new DFAState(name);
            setOfStates.add(startState);
        }
    }

    @Override
    public void addState(String name) {
        // Create a new state and add it to the set of states
        DFAState newState = new DFAState(name);
        setOfStates.add(newState);

    }

    @Override
    public void addFinalState(String name) {
        DFAState finalState = new DFAState(name);
        boolean alreadyExists = false;
        // Check if our finalState already exists in the set of states, and set it to a
        // final state if so
        for (DFAState state : setOfStates) {
            if (state.toString().compareTo(name) == 0) {
                state.setFinalState();
                finalStates.add(state);
                alreadyExists = true;
            }
        }
        // If we didn't find the final state in our set of states, make it a final state
        // and add it to the set
        if (!alreadyExists) {
            finalState.setFinalState();
            setOfStates.add(finalState);
            finalStates.add(finalState);
        }

    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
        DFAState fState = null;
        DFAState tState = null;

        // Find our fromState and toState in the set of states
        for (DFAState state : setOfStates) {
            if (state.toString().compareTo(fromState) == 0) {
                fState = state;
            }
            if (state.toString().compareTo(toState) == 0) {
                tState = state;
            }
        }
        // Create our transition for the transition table using a hash map
        HashMap<DFAState, Character> hMap = new HashMap<DFAState, Character>();
        hMap.put(fState, onSymb);
        transitionTable.put(hMap, tState);

        // Add the symbol to our alphabet
        alphabet.add(onSymb);
    }

    @Override
    public Set<? extends State> getStates() {
        // Return set of states
        return setOfStates;
    }

    @Override
    public Set<? extends State> getFinalStates() {
        // return set of final states
        return finalStates;
    }

    @Override
    public State getStartState() {
        // return the start state
        return startState;
    }

    @Override
    public Set<Character> getABC() {
        // return the alphabet
        return alphabet;
    }

    /**
     * Construct the textual representation of the DFA, for example
     * A simple two state DFA
     * Q = { a b }
     * Sigma = { 0 1 }
     * delta =
     * 0 1
     * a a b
     * b a b
     * q0 = a
     * F = { b }
     * 
     * The order of the states and the alphabet is the order
     * in which they were instantiated in the DFA.
     * 
     * @return String representation of the DFA
     */
    public String toString() {
        return "Q = { " + printStates() + " }" + "\n"
                + "Sigma = { " + printSigma() + " }" + "\n"
                + "Delta = " + "\n" + printTransitionTable()
                + "q0 = " + startState.toString() + "\n"
                + "F = { " + printFinalStates() + " }" + "\n";
    }


    /**
     * Prints the states in a readable form for toString
     * 
     * @return - the formatted string representation of the states in Q
     */
    private String printStates() {
        // Get the states
        String qString = "";
        for (DFAState state : setOfStates) {
            if (qString.compareTo("") == 0) {
                qString = state.toString();
            } else {
                qString = qString + " " + state.toString();
            }
        }
        return qString;
    }

    /**
     * Prints the set of alphabet characters in a more readable form for toString
     * 
     * @return - the formatted string of alphabet characters
     */
    private String printSigma() {
        // Get alphabet
        String sigmaString = "";
        for (Object obj : alphabet) {
            if (sigmaString.compareTo("") == 0) {
                sigmaString = obj.toString();
            } else {
                sigmaString = sigmaString + " " + obj.toString();
            }
        }
        return sigmaString;
    }

    /**
     * Prints the transition table in a more readable form for toString
     * 
     * @return - the formatted string representation of the transition table
     */
    private String printTransitionTable() {
        String header = "";
        String bodyWithLabels = "";
        String returnString = "\t\t";

        // Get the horizontal header of the transition table
        for (Object obj : alphabet) {
            if (header.compareTo("") == 0) {
                header = obj.toString();
            } else {
                header += "\t" + obj.toString();
            }
        }

        // Add header to the return String
        returnString += header + "\n";

        // Get the body of the transition table
        for (DFAState state : setOfStates) {
            // Get our vertical headers
            bodyWithLabels += "\t" + state + "\t";
            //
            for (Object obj : alphabet) {
                String alphabetChar = obj.toString();
                DFAState toState = (DFAState) getToState(state, alphabetChar.charAt(0));
                if (toState != null){
                    bodyWithLabels += toState.toString() + "\t";
                }
                
            }
            bodyWithLabels += "\n";
        }

        // Add table body to return string
        returnString += bodyWithLabels;

        return returnString;
    }

    /**
     * Prints the set of final states in a more readable form for toString
     * 
     * @return - the formatted string of final states
     */
    private String printFinalStates() {
        // Get set of final states
        String fString = "";
        for (DFAState state : finalStates) {
            if (fString.compareTo("") == 0) {
                fString = state.toString();
            } else {
                fString = fString + " " + state.toString();
            }
        }
        return fString;
    }

}