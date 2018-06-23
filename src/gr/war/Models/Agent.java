package gr.war.Models;

import gr.war.Algos.AlgorithPrvovider;
import gr.war.Models.GraphModel.Graph;
import gr.war.Models.GraphModel.GraphNode;
import gr.war.Services.ScenarioService;
import gr.war.Singletons.Manager;
import gr.war.Utils.Constants;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Agent extends Task<Object> {

    protected final Team team;
    protected final CollectionStrategy collectionStrategy;
    protected final ScenarioService scenarioService;
    protected final Graph graph;
    protected final GridPoint startLocation;
    protected final AgentPlan agentPlan;
    protected AgentStatistics agentStatistics;
    protected GraphNode rootNode;

    //AlgorithmProvider
    protected AlgorithPrvovider algorithPrvovider;

    //Position n' Situation
    protected boolean isInEdges = false;
    protected boolean IsCarrying = false;
    protected GridPoint currentLocation;


    public Agent(ScenarioService scenarioService, Team team, CollectionStrategy collectionStrategy, GridPoint startLocation, AgentPlan agentPlan) {

        this.scenarioService = scenarioService;
        this.team = team;
        this.collectionStrategy = collectionStrategy;
        this.startLocation = startLocation;
        this.agentPlan = agentPlan;

        this.rootNode = new GraphNode();
        this.rootNode.setPoint(startLocation);
        this.rootNode.setStadiumMaterials(StadiumMaterials.HOUSE);
        this.graph = new Graph();
        this.agentStatistics = new AgentStatistics();
        this.agentStatistics.setTeam(this.team);

    }

    protected void SetAlgorithmConditions(){

        StadiumEnvironment stadiumEnvironment = scenarioService.getStadiumEnvironment();
        algorithPrvovider = AlgorithPrvovider.create(stadiumEnvironment.getWidth(), stadiumEnvironment.getHeight(), rootNode);

        if(  (startLocation.getX() == 0 || startLocation.getX() == Constants.MAX_WIDTH ) ||
                ( startLocation.getY() == 0 || startLocation.getY() == Constants.MAX_HEIGHT)
                ){

            isInEdges = true;
        }

    }

    protected synchronized GridPoint GetNextLocation(){

        /*
        if( isInEdges )
            return algorithPrvovider.getBFS().GetNextLocation();
        else
            return algorithPrvovider.getLVN().GetNextLocation(currentLocation, graph);
         */

        return algorithPrvovider.getLVN().GetNextLocation(currentLocation, graph);

    }

    protected void WalkToPoint(final GridPoint gridPointOld, final GridPoint gridPointNew) {
        // Initialize the image from the current(old) location that will override the agent.
        StadiumMaterials material = scenarioService.getStadiumEnvironment().getStadiumMaterialsOfPoint(gridPointOld);

        Image imagePlace = new Image(getClass().getResourceAsStream(Constants.IMG_STADIUM_MATERIALS + material.getMaterialName()));
        Image imageAgent;

        final GridImgView imageViewPlace = new GridImgView(imagePlace);
        imageViewPlace.setMaterial(material.getMaterialVocabulary());

        if( team.equals(Team.TEAM_B)) {
            //imageViewPlace.setMaterial(StadiumMaterials.AGENTB.getMaterialVocabulary());
            imageAgent = new Image(getClass().getResourceAsStream(Constants.IMG_STADIUM_MATERIALS + StadiumMaterials.AGENTB.getMaterialName()));
        }
        else {
            //imageViewPlace.setMaterial(StadiumMaterials.AGENTA.getMaterialVocabulary());
            String path = Constants.IMG_STADIUM_MATERIALS + StadiumMaterials.AGENTA.getMaterialName();
            InputStream resource = getClass().getResourceAsStream(path);
            imageAgent = new Image(resource);
        }
        imageViewPlace.setFitWidth(40);

        System.out.println("Agent:["+ this.toString() + "]" + "(" + gridPointOld.getX() + ',' + gridPointOld.getY() + ")" + "--> " + "(" + gridPointNew.getX() + ',' + gridPointNew.getY() + ")");


        final GridImgView imageViewAgent = new GridImgView(imageAgent);
        imageViewAgent.setFitWidth(20);

        this.agentStatistics.setAgentStepCount(this.agentStatistics.getAgentStepCount());


        // Create the agent to the next location.
        Platform.runLater(() -> {
            GridPane gridPane = Manager.getInstance().getGrid();
            gridPane.add(imageViewPlace, gridPointOld.getX(), gridPointOld.getY());
            gridPane.add(imageViewAgent, gridPointNew.getX(), gridPointNew.getY());
        });

        // Update the step count for statistics.
        //agentStatistics.setAgentStepCount( agentStatistics.getAgentStepCount() + 1);
    }


    protected void checkScenarioState() throws InterruptedException {
        // Check the scenario state.
        while (scenarioService.getScenarioState() != ScenarioState.RUNNING) {
            if (scenarioService.getScenarioState() == ScenarioState.STOPPED) {
                //this.cancel();
            }
            sleepAgent(50);
        }
    }



    protected void sleepAgent() throws InterruptedException {
        Random random = ThreadLocalRandom.current();
        int sleepTime = 100 + 300;
        //logger.debug("Agent" + id + " - Sleeping for " + sleepTime + "ms.");
        if( this.team.equals(Team.TEAM_B )) sleepTime = sleepTime / 2;
        sleepAgent(sleepTime);
    }

    protected void sleepAgent(long timeToSleep) throws InterruptedException {
        Thread.sleep(timeToSleep);
    }

    @Override
    protected Object call() throws Exception {
        return null;
    }

    public AlgorithPrvovider getAlgorithPrvovider() {
        return algorithPrvovider;
    }

    public void setAlgorithPrvovider(AlgorithPrvovider algorithPrvovider) {
        this.algorithPrvovider = algorithPrvovider;
    }

    public Graph getGraph() {
        return graph;
    }

    public AgentStatistics getAgentStatistics() {
        return agentStatistics;
    }
}
