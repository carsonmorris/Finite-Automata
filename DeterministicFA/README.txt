****************
* Project 1 - Deterministic Finite Automata
* Class: CS361
* Date: September 2022
* Carson Morris, Joseph Cierzan
**************** 

OVERVIEW:

 This is a program that models a deterministic finite automata. The program takes in a file that specifies an initial state, 
 a set of final states, and the state transitions. 


INCLUDED FILES:

fa - folder containing all program files
 * FAInterface.java - Interface that defines methods for DFA creation
 * State.java - Abstract class for individual states in the DFA
 * dfa - subfolder containing driver and supporting files
    - DFA.java - The implementation of the methods in FAInterface and DFAInterface
    - DFADriver.java - The driver class that runs the program and takes in text files as input to model a DFA
    - DFAInterface.java - Interface that extends FAInterface
    - DFAState.java - Class that extends the State class
 tests - folder containing our test input files
  * p1tc1.txt
  * p1tc2.txt
  * p1tc3.txt
  * p1tc4.txt
  * p1tc5.txt
  * p1tc6.txt
  * p1tc7.txt
  * p1tc8.txt
  * p1tc9.txt
  * p1tc10.txt


COMPILING AND RUNNING:

To compile fa.dfa.DFADriver from the top directory of these files:
$javac fa/dfa/DFADriver.java

To run fa.dfa.DFADriver, put your input file in the tests folder and run:
$java fa.dfa.DFADriver ./tests/[your input file name].txt

The console will display the list of states, the alphabet being used, the transition table, the start state, and the set of 
final states. Then, for each string being tested, it prints "yes" if the string is accepted, or "no" if the string is not accepted.

PROGRAM DESIGN AND IMPORTANT CONCEPTS:

For this program we were given the DFADriver along with the Interface and Abstract classes. To model a DFA using what we were given, we implemented the methods laid out for us in the interfaces by utilizing a hashmap to keep track of our given information for transitions and we used a boolean to determine if a given state is a final state. The program parses the given string and uses hashmaps to keep track of the current state after each transition. Once the string is parsed (and based on the boolean for a final state) we determine if the string is accepted by the language or not. 

TESTING:

To test our DFA, we were given 3 test cases and created 7 of our own. These tests used varying state names, alphabets, and strings
to check. We created tests for: cyclic and noncyclic DFAs, a DFA with no alphabet, a DFA of a single state with 1 alphabet letter,
a DFA with 2 alphabet letters, a DFA with 3 alphabet letters, etc.

 
----------------------------------------------------------------------------
