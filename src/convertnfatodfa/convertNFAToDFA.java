package convertnfatodfa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class convertNFAToDFA {

    private static int numberOfStates;
    public static char[][] connectionNFA; //This array for svae connection between states in NFA
    private static Map<String, String> connectionDFA; //This array for svae connection between states in DFA
    private static ArrayList<String> tempEpsilonClosure; //This Array for calculating epsilon closure for one state in specific time.
    private static LinkedList<ArrayList<String>> statesDFA;//This list for save all epsilon closure.
    private static ArrayList<Character> allCharacters;//This are for save that characters that on the edge.Ex: a,b || 0,1
    private final static char epsilon = 'ε';

    public convertNFAToDFA(int number_of_state) {
        numberOfStates = number_of_state;//************
        connectionNFA = new char[numberOfStates][numberOfStates];
        for (char[] cs : connectionNFA) {
            Arrays.fill(cs, '0');
        }
        statesDFA = new LinkedList();
        connectionDFA = new HashMap<>();
        allCharacters = new ArrayList();
        connectionDFA = new HashMap<>();
    }

    public static void setConnectionNFA(char[][] connectionNFA) {
        convertNFAToDFA.connectionNFA = connectionNFA;
    }

    public static void setAllCharacters(ArrayList<Character> allCharacters) {
        convertNFAToDFA.allCharacters = allCharacters;
    }
    
    public static char[][] getConnectionNFA() {
        return connectionNFA;
    }

    public static Map<String, String> getConnectionDFA() {
        return connectionDFA;
    }

    public static char[] getAllCharacters() {
        return allCharacters.toString().toCharArray();
    }

    public static LinkedList<ArrayList<String>> getStatesDFA() {
        return statesDFA;
    }

    public static int getNumberOfStates() {
        return numberOfStates;
    }
    
    public static void calculateEpsilonClosure(ArrayList<Integer> move) {
        tempEpsilonClosure = new ArrayList();
        for (Integer name : move) {
            epsilonClosureForOneSatate(name);
        }
        if (!statesDFA.contains(tempEpsilonClosure)) {
            statesDFA.add(tempEpsilonClosure);
        }
    }

    private static void epsilonClosureForOneSatate(Integer name) {
        tempEpsilonClosure.add(name.toString());
        for (int i = 0; i < numberOfStates; i++) {
            if (connectionNFA[name][i] == epsilon) {
                epsilonClosureForOneSatate(i);
            }
        }
    }

    //Take name of state in DFA and the character that I want go to it.
    //Then return state that can reach it. 
    public static ArrayList<Integer> move(int stateName, char key) {
        ArrayList<String> temp = statesDFA.get(stateName);
        ArrayList<Integer> result = new ArrayList();
        int name;
        for (String n : temp) {
            name = Integer.parseInt(n);
            for (Integer i = 0; i < numberOfStates; i++) {
                if (connectionNFA[name][i] == key) {
                    result.add(i);
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public static void convert() {
        ArrayList<Integer> tempMove = new ArrayList();
        tempMove.add(0);
        calculateEpsilonClosure(tempMove);
        for (int i = 0; i < statesDFA.size(); i++) {
            for (int j = 0; j < allCharacters.size(); j++) {
                tempMove = move(i, allCharacters.get(j));
                if (tempMove != null) {
                    calculateEpsilonClosure(tempMove);
                    connectionDFA.put(i + " " + allCharacters.get(j), "" + statesDFA.indexOf(tempEpsilonClosure));
                }
            }
        }
    }
}
