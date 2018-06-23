package gr.war.Models.GraphModel;

import gr.war.Models.GridPoint;
import gr.war.Models.StadiumMaterials;

import java.util.List;


public class GraphNode {

    private GraphNode parent;
    private GridPoint Point;
    private gr.war.Models.StadiumMaterials StadiumMaterial;
    private List<StadiumMaterials> NeighborMaterials; // Maximum 4 items.
    private List<GraphNode> Childrent = null;
    private int VisitedTimes;

    public GridPoint getPoint() {
        return Point;
    }

    public void setPoint(GridPoint point) {
        Point = point;
    }

    public StadiumMaterials getStadiumMaterials() {
        return StadiumMaterial;
    }

    public void setStadiumMaterials(gr.war.Models.StadiumMaterials stadiumMaterials) {
        StadiumMaterial = stadiumMaterials;
    }

    public List<StadiumMaterials> getNeighborMaterials() {
        return NeighborMaterials;
    }

    public void setNeighborMaterials(List<gr.war.Models.StadiumMaterials> neighborMaterials) {
        NeighborMaterials = neighborMaterials;
    }

    public int getVisitedTimes() {
        return VisitedTimes;
    }

    public void setVisitedTimes(int visitedTimes) {
        VisitedTimes = visitedTimes;
    }

    public GraphNode getParent() {
        return parent;
    }

    public void setParent(GraphNode parent) {
        this.parent = parent;
    }

    public List<GraphNode> getChildrent() {
        return Childrent;
    }

    public void setChildrent(List<GraphNode> childrent) {
        Childrent = childrent;
    }


    public GraphNode() {

    }



    public GraphNode(GridPoint point, int visitedTimes) {
        Point = point;

        VisitedTimes = visitedTimes;
    }

    public GraphNode(GraphNode graphNode) {
        Point = graphNode.getPoint();
        StadiumMaterial = graphNode.getStadiumMaterials();
        NeighborMaterials = graphNode.getNeighborMaterials();
        VisitedTimes = graphNode.getVisitedTimes();
    }
}
