package re;
import fa.State;
import fa.nfa.NFA;
import fa.nfa.NFAState;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author Carson Morris  
 * @Author Joseph Cierzan
 * A class that implements REInterface to parse a given regular expression and determine if a provided string is accepted by the regular expression.
 */
public class RE implements REInterface {
	String regExpr = "";
	int stateNum = 0;
	
	/**
	 * Constructor
	 * @param regExpr - the string describing our regular expression
	 */
	public RE(String regExpr) {
		this.regExpr = regExpr;
	}

	/**
	 * Get an NFA based on our Regular Expression string in its current state when called
	 * @return - The NFA built from the regular expression
	 */
	public NFA getNFA() {
		NFA regexSection = new NFA();
		
		//Start building the NFA by section
		while(regExpr.length() > 0 && regExpr.charAt(0) != ')' && regExpr.charAt(0) != '|'){
			NFA fact = factor();
		

			//If there is a star*, handle it
			while(regExpr.length() > 0 && regExpr.charAt(0) == '*'){
				this.regExpr = this.regExpr.substring(1);
				fact = star(fact);
			}
			//Otherwise make the simple NFA
			if(regexSection.getStates().isEmpty()){
				regexSection = fact;
			//If there are multiple terms, concatenate them
			}else{
				regexSection = concatParts(regexSection, fact);
			}
		}

		//If there is a union with "|", call getNFA again on this new section and build the NFA
		if (regExpr.length() > 0 && regExpr.charAt(0) == '|') {
			this.regExpr = this.regExpr.substring(1);
			NFA regExpr = getNFA();
			
			//Make the new NFA we will union our two parts with
			NFA combinedNFAs = new NFA();
		
			//Create the new start states
			String startName = "q" + stateNum;
			stateNum++;
			combinedNFAs.addStartState(startName);

			//Add all the states, transitions, and alphabet chars from the other NFAs
			combinedNFAs.addNFAStates(regexSection.getStates());
			combinedNFAs.addNFAStates(regExpr.getStates());
			combinedNFAs.addTransition(startName, 'e', regexSection.getStartState().getName());
			combinedNFAs.addTransition(startName, 'e', regExpr.getStartState().getName());
			combinedNFAs.addAbc(regexSection.getABC());
			combinedNFAs.addAbc(regExpr.getABC());

			return combinedNFAs;

		//Otherwise, return the NFA we already built
		} else {
			return regexSection;
		}
	}


	/**
	 * This method will make an NFA from separate chunks of the regex, such as those in parenthesis
	 * @return - an NFA built from the char or what is within parenthesis
	 */
	private NFA factor() {
		//Check for a parenthesis
		switch (regExpr.charAt(0)){
		case '(':
			//If there is a parenthesis, build an NFA for whats between them
			this.regExpr = this.regExpr.substring(1);
			NFA reg = getNFA();
			this.regExpr = this.regExpr.substring(1);
			return reg;
		//Otherwise
		default:
			char regexChar = regExpr.charAt(0);
			this.regExpr = this.regExpr.substring(1);
			
			//Make a new NFA with 2 states and a transition on the front-most char
			NFA nfa = new NFA();
			String state = "q" + stateNum;
			stateNum++;

			//Adds the start state
			//Add the start state, final state, transitions, etc
			nfa.addStartState(state);
			String finalState = "q" + stateNum;
			stateNum++;
			nfa.addFinalState(finalState);
			nfa.addTransition(state, regexChar, finalState);
			Set<Character> alphabet = new LinkedHashSet<Character>();
			alphabet.add(regexChar);
			nfa.addAbc(alphabet);
			return nfa;
		}
	}

	/**
	 * This creates an NFA for when a star is present (zero or more of a certain sequence)
	 * @param factor - the NFA we are putting the star operation on
	 * @return NFA - the new NFA with a star operation
	 */
	private NFA star(NFA factor) {

		//Creates the NFA to return 
		NFA nfa = new NFA();
		
		//Make and add new start states, final states, and add the transitions from factor
		String start = "q" + stateNum;
		stateNum++;
		nfa.addStartState(start);
		String finalState = "q" + stateNum;
		stateNum++;
		nfa.addFinalState(finalState);
		nfa.addNFAStates(factor.getStates());
		//Add empty transition for "0 or more"
		nfa.addTransition(start, 'e', finalState);
		nfa.addTransition(finalState, 'e', factor.getStartState().getName());
		nfa.addTransition(start, 'e', factor.getStartState().getName());
		//Include alphabet chars
		nfa.addAbc(factor.getABC());
		
		//Add empty transitions from old final states to new final state
		for(State state1: factor.getFinalStates()) {
			nfa.addTransition(state1.getName(), 'e', finalState);
			for(State state2: nfa.getFinalStates()){
				if(state2.getName().equals(state1.getName())) {
					((NFAState)state2).setNonFinal();
				}
			}
		}

    	return nfa;
	}
	
	/**
	 * Concat two NFAs togethers 
	 * @param firstPart - the first NFA 
	 * @param secondPart - the NFA that we will attach to the first NFA
	 * @return - the newly concatenated NFA
	 */
	private NFA concatParts(NFA firstPart, NFA secondPart) {
		//Grab the states from our two parts
		String secondPartStart = secondPart.getStartState().getName();
		Set<State> firstPartFinals = firstPart.getFinalStates();
		//Add all states from second NFA to first NFA
		firstPart.addNFAStates(secondPart.getStates());
		//Add transitions from the first NFA's final states to the start state of the second NFA
		for(State state: firstPartFinals) {
			((NFAState)state).setNonFinal();
			firstPart.addTransition(state.getName(), 'e', secondPartStart);
		}
		//Include both alphabets
		firstPart.addAbc(secondPart.getABC());
		
		return firstPart;
	}

}
