package gr.war.Singletons;

import gr.war.Models.*;
import gr.war.Services.AgentService;
import gr.war.Services.ScenarioService;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;


public class Manager {

    private static Manager Instance;

    private StadiumEnvironment environment;
    private List<AgentService> agentServices = new ArrayList<>();
    private ScenarioService scenarioService;
    private Village villageA, villageB;
    private ScrollPane gridContainer;
    private GridPane grid;


    public static Manager getInstance(){

        if( Instance == null )
            Instance = new Manager();

        return Instance;
    }

    public void InitVillages(){

        for( Village village : environment.getVillages())
            if( village.getTeam().equals(Team.TEAM_A))
                villageA = village;
            else
                villageB = village;
    }

    public List<AgentService> getAgentServices() {
        return agentServices;
    }

    public void setAgentServices(List<AgentService> agentServices) {
        this.agentServices = agentServices;
    }

    public ScrollPane getGridContainer() {
        return gridContainer;
    }

    public void setGridContainer(ScrollPane gridContainer) {
        this.gridContainer = gridContainer;
    }

    public GridPane getGrid() {
        return grid;
    }

    public void setGrid(GridPane grid) {
        this.grid = grid;
    }

    public Village getVillageA() {
        return villageA;
    }

    public void setVillageA(Village villageA) {
        this.villageA = villageA;
    }

    public Village getVillageB() {
        return villageB;
    }

    public void setVillageB(Village villageB) {
        this.villageB = villageB;
    }

    public StadiumEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(StadiumEnvironment environment) {
        this.environment = environment;
    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    public void InitComponents(){
        Village A = villageA,
                B = villageB;

        this.agentServices.clear();

        for(String key : A.getTargetsPortion().keySet()){

            AgentPlan agentPlan = new AgentPlan("", A.getTargets(), A.getTargetsPortion(), StadiumMaterials.valueOf(key.toUpperCase()), CollectionStrategy.SPECIFIC);
            GridPoint home = A.getStartlocation();
            AgentService agentService = new AgentService(scenarioService, Team.TEAM_A, CollectionStrategy.SPECIFIC,home, agentPlan );
            this.agentServices.add(agentService);
            //break;

        }


        for(String key : B.getTargetsPortion().keySet()){

            AgentPlan agentPlan = new AgentPlan("", B.getTargets(), B.getTargetsPortion(), StadiumMaterials.valueOf(key.toUpperCase()), CollectionStrategy.ALL);
            GridPoint home = B.getStartlocation();
            AgentService agentService = new AgentService(scenarioService, Team.TEAM_B, CollectionStrategy.ALL,home, agentPlan );
            this.agentServices.add(agentService);
            //break;

        }


        // Start the agents.
        for ( AgentService agentService : Manager.getInstance().getAgentServices()) {
            Thread agentThread = new Thread(agentService);
            agentThread.setDaemon(true);
            agentThread.start();
        }

        // Start the scenario.
        Thread scenarioThread = new Thread(scenarioService);
        scenarioThread.setDaemon(true);
        scenarioThread.start();
    }
}
