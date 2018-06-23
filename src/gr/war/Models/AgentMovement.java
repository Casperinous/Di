package gr.war.Models;


public class AgentMovement {

    private GridPoint point;
    private StadiumMaterials stadiumMaterials;

    public AgentMovement(GridPoint point, StadiumMaterials stadiumMaterials) {
        this.point = point;
        this.stadiumMaterials = stadiumMaterials;
    }

    public GridPoint getPoint() {
        return point;
    }

    public void setPoint(GridPoint point) {
        this.point = point;
    }

    public StadiumMaterials getStadiumMaterials() {
        return stadiumMaterials;
    }

    public void setStadiumMaterials(StadiumMaterials stadiumMaterials) {
        this.stadiumMaterials = stadiumMaterials;
    }
}
