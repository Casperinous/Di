package gr.war.Models;

import gr.war.Models.GraphModel.GraphNode;

import java.util.List;


public class ExchangableKnowledge {



    protected List<List<GraphNode>> graphNodes;

    public List<List<GraphNode>> getGraphNodes() {
        return graphNodes;
    }

    public void setGraphNodes(List<List<GraphNode>> graphNodes) {
        this.graphNodes = graphNodes;
    }

    public GridPoint findTargetMaterial(StadiumMaterials material, StadiumEnvironment stadiumEnvironment){

        GridPoint gpoint = null;

        if ( graphNodes == null )
            return gpoint;

        for( int i = 0, y = graphNodes.size(); i < y; i++ ){

            List<GraphNode> nodes = graphNodes.get(i);

            for( int x = 0, p = nodes.size(); x < p; x++){

                if( nodes.get(x).getStadiumMaterials().equals(material)){

                    gpoint = nodes.get(x).getPoint();
                    return gpoint;
                }

            }

        }
        return gpoint;

    }


}
