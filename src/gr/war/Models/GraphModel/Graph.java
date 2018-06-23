package gr.war.Models.GraphModel;


import gr.war.Models.GridPoint;
import gr.war.Models.StadiumMaterials;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Graph {

    private final Set<GraphNode> unique;
    private final Set<GraphNode> nodeSet;
    private final Set<GraphLine> lineSet;

    public Graph() {
        this.unique = new HashSet<>();
        this.nodeSet = new HashSet<>();
        this.lineSet = new HashSet<>();
    }

    public Set<GraphNode> getUnique() {
        return unique;
    }

    public Set<GraphNode> getNodeSet() {
        return nodeSet;
    }

    public Set<GraphLine> getLineSet() {
        return lineSet;
    }

    public GraphNode findByPoint(GridPoint point) {
        if( point == null ) return null;
        for (GraphNode node : nodeSet) {
            if (node.getPoint().equals(point)) {
                return node;
            }
        }
        return null;
    }


    public GraphNode findByUniqueSet(GridPoint point){

        for (GraphNode node : unique) {
            if (node.getPoint().equals(point)) {
                return node;
            }
        }
        return null;
    }

    /*
    public GraphNode findByMaterialInNeighbors(StadiumMaterials stadiumMaterial){



    }*/

    public GraphNode findByMaterial(StadiumMaterials stadiumMaterial) {
        for (GraphNode node : nodeSet) {
            if (node.getStadiumMaterials().equals(stadiumMaterial)) {
                return node;
            }
            /*
            for (StadiumMaterials current : node.getNeighborMaterials()) {
                if (current.equals(stadiumMaterial)) {
                    return node;
                }
            }*/
            if( node.getChildrent() != null ) {
                for (GraphNode childs : node.getChildrent()) {
                    if (childs.getStadiumMaterials().equals(stadiumMaterial)) {
                        return childs;
                    }
                }
            }

        }
        return null;
    }
}
