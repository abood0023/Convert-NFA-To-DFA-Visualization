package convertnfatodfa;

import com.mxgraph.view.mxGraph;

public class MyGraph extends mxGraph {

    @Override
    public boolean isCellSelectable(Object cell) {
        if (model.isEdge(cell)) {
            return false;
        }
        return super.isCellSelectable(cell);
    }

    @Override
    public boolean isCellEditable(Object o) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

}
