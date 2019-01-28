package convertnfatodfa;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import javax.swing.JFrame;
import com.mxgraph.swing.mxGraphComponent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

public class Visualization extends JFrame implements WindowListener, ActionListener {

    //private static final long serialVersionUID = -2707712944901661771L;

    private static MyGraph graph = new MyGraph();
    Object statesPosNFA[], statesPosDFA[], edgesPosDFA[], edgesPosNFA[];

    public Visualization() {
        super("Visualization of the convert NFA to DFA thaThomson's Construction");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                deleteGraph();
            }
        });
        setResizable(false);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        statesPosNFA = null;
        statesPosDFA = null;
        edgesPosDFA = null;
        edgesPosNFA = null;
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);//disable adding edges
        getContentPane().add(graphComponent);
        graph.setDropEnabled(false);//disable drop vertex on the edge
        graph.setAllowDanglingEdges(false);
        graph.setCellsResizable(false);

        graphComponent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public void drawStatesNFA(int number_of_states) {
        statesPosNFA = new Object[number_of_states];
        int x = 260, y = 150;
        statesPosNFA[number_of_states - 1] = graph.insertVertex(graph.getDefaultParent(), null, "S " + (number_of_states - 1), x, y, 40, 40, "shape=doubleEllipse;fontSize=14");

        for (int i = 0; i < number_of_states - 1; i++) {
            statesPosNFA[i] = graph.insertVertex(graph.getDefaultParent(), null, "S " + i, x, y, 40, 40, "shape=ellipse;fontSize=14");
            x += 50;
        }

        Object startCell = graph.insertVertex(graph.getDefaultParent(), null, "", 190, 150, 40, 40, "strokeColor=none;fillColor=none");
        graph.insertEdge(graph.getDefaultParent(), null, "Start     ", startCell, statesPosNFA[0], "fontStyle=1;fontSize=14;fontColor=#04B431;rotatio=100");
    }

    public void drawStatesDFA(LinkedList<ArrayList<String>> statesDFA, int number_of_states_NFA) {
        System.out.println("from drawStatesDFA: " + statesDFA);
        for (ArrayList<String> arrayList : statesDFA) {
            System.out.println(arrayList);
        }
        statesPosDFA = new Object[statesDFA.size()];
        String last_state_number = "" + (number_of_states_NFA - 1);
        int x = 260, y = 150, i = 0;
        for (ArrayList<String> arrayList : statesDFA) {
            if (arrayList.contains(last_state_number)) {
                statesPosDFA[i] = graph.insertVertex(graph.getDefaultParent(), null, "S " + i, x, y, 40, 40, "shape=doubleEllipse;fontSize=14;fontStyle=1;fontColor=#C0392B;");
            } else {
                statesPosDFA[i] = graph.insertVertex(graph.getDefaultParent(), null, "S " + i, x, y, 40, 40, "shape=ellipse;fontSize=14;fontStyle=1;fontColor=#C0392B;");
            }
            x += 50;
            i++;
        }
        Object startCell = graph.insertVertex(graph.getDefaultParent(), null, "", 190, 150, 40, 40, "strokeColor=none;fillColor=none");
        graph.insertEdge(graph.getDefaultParent(), null, "Start     ", startCell, statesPosDFA[0], "fontStyle=1;fontSize=14;fontColor=#04B431;");
    }

    public void drawEdgesNFA(char[][] connectNFA) {
        ArrayList<Object> pos = new ArrayList<>();
        for (int i = 0; i < connectNFA.length; i++) {
            for (int j = 0; j < connectNFA[i].length; j++) {
                if (connectNFA[i][j] != '0') {
                    pos.add(graph.insertEdge(graph.getDefaultParent(), null, connectNFA[i][j], statesPosNFA[i], statesPosNFA[j], "strokeWidth=0.5;fontSize=20;fontColor=#C0392B;"));
                }
            }
        }
        mxHierarchicalLayout x = new mxHierarchicalLayout(graph);
        x.setOrientation(SwingConstants.WEST);
        x.execute(graph.getDefaultParent());
        edgesPosNFA = pos.toArray();
    }

    public void drawEdgesDFA(Map<String, String> connectionDFA) {
        edgesPosDFA = new Object[connectionDFA.keySet().size()];
        System.out.println("from drawEdgesDFA: " + connectionDFA.keySet());
        int from, to, i = 0;
        for (String string : connectionDFA.keySet()) {
            from = Integer.parseInt(string.substring(0, 1));
            to = Integer.parseInt(connectionDFA.get(string));
            if (from == to) {
                graph.setAllowLoops(true);
                edgesPosDFA[i] = graph.insertEdge(graph.getDefaultParent(), null, string.charAt(2), statesPosDFA[from], statesPosDFA[to], "rounded=1;strokeWidth=0.5;fontSize=20;fontColor=#C0392B;");
            } else {
                edgesPosDFA[i] = graph.insertEdge(graph.getDefaultParent(), null, string.charAt(2), statesPosDFA[from], statesPosDFA[to], "strokeWidth=0.5;fontSize=20;fontColor=#C0392B;");
            }
            i++;
        }
        new mxCircleLayout(graph).execute(graph.getDefaultParent());
        new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
    }

    public void drawGraphDFA(LinkedList<ArrayList<String>> statesDFA, int number_of_states_NFA, Map<String, String> connectionDFA) {
        drawStatesDFA(statesDFA, number_of_states_NFA);
        drawEdgesDFA(connectionDFA);
    }

    public void drawGraphNFA(int number_of_states, char[][] connectNFA) {
        drawStatesNFA(number_of_states);
        drawEdgesNFA(connectNFA);
    }

    public void deleteGraph() {
        if (edgesPosDFA != null) {
            for (Object object : statesPosDFA) {
                graph.getModel().remove(object);
            }
            for (Object object : edgesPosDFA) {
                graph.getModel().remove(object);
            }
        } else if (edgesPosNFA != null) {
            for (Object object : statesPosNFA) {
                graph.getModel().remove(object);
            }
            for (Object object : edgesPosNFA) {
                graph.getModel().remove(object);
            }

        }
        edgesPosDFA = null;
        edgesPosNFA = null;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
