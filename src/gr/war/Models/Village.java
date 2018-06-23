package gr.war.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Village {

    private Team team;
    private List<StadiumMaterials> targets;
    private Map<String, Integer> targetsPortion;
    private int agents;
    private GridPoint startlocation;


    private Village(){
        targets = new ArrayList<>();
        targetsPortion = new HashMap<>();
    }

    public boolean IsInTarget(StadiumMaterials target){

        boolean result = false;
        for( int i = 0, y = targets.size(); i < y; i++){
            if( targets.get(i).equals(target)){
                result = true;
                break;
            }
        }

        return result;
    }


    public static Village create(){
        return new Village();
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<StadiumMaterials> getTargets() {
        return targets;
    }

    public void setTargets(List<StadiumMaterials> targets) {
        this.targets = targets;
    }

    public Map<String, Integer> getTargetsPortion() {
        return targetsPortion;
    }

    public void setTargetsPortion(Map<String, Integer> targetsPortion) {
        this.targetsPortion = targetsPortion;
    }

    public int getAgents() {
        return agents;
    }

    public void setAgents(int agents) {
        this.agents = agents;
    }

    public GridPoint getStartlocation() {
        return startlocation;
    }

    public void setStartlocation(GridPoint startlocation) {
        this.startlocation = startlocation;
    }
}
