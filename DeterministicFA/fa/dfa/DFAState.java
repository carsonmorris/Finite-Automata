package fa.dfa;

/**
 * @Authors Carson Morris and Joseph Cierzan
 * CS 361
 * Extends State.java
 */

public class DFAState extends fa.State{
    private boolean finalState;

    /**
     * Default constructor
     * @param label - the state label
     */
    public DFAState(String label){
        super.name = label;
        this.finalState = false;
    }

    /**
     * Set the DFA State to a final state
     */
    protected void setFinalState(){
        this.finalState = true;
    }


    /**
     * Check if the state is a final state
     * @return - the boolean for being a final state
     */
    protected boolean isFinalState(){
        return this.finalState;
    }


}