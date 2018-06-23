package gr.war.Services;

import gr.war.Algos.BreadthFirstSearch;
import gr.war.Models.*;
import gr.war.Models.GraphModel.Graph;
import gr.war.Models.GraphModel.GraphLine;
import gr.war.Models.GraphModel.GraphNode;
import gr.war.Singletons.Manager;
import gr.war.Utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class AgentService extends Agent {

    private StadiumMaterials carryingMaterial = null;
    private boolean IsFinishedTask = false;

    public AgentService(ScenarioService scenarioService, Team team, CollectionStrategy collectionStrategy, GridPoint startLocation, AgentPlan agentPlan) {
        super(scenarioService, team, collectionStrategy, startLocation, agentPlan);
    }


    @Override
    protected Object call() throws Exception {
        GridPoint oldLocation = startLocation;
        currentLocation = startLocation;
        GridPoint nextLocation = startLocation;
        SetAlgorithmConditions();


        try {
            // Loop till the agent complete his plan.
            boolean isPlanCompleted = false;
            while (true) {
                // Check the scenario state.
                checkScenarioState();

                if ( currentLocation.equals( startLocation) && IsCarrying && carryingMaterial != null){
                    System.out.println("Updating Material : " + carryingMaterial.getMaterialVocabulary());
                    UpdateMaterialTarget(carryingMaterial);
                    carryingMaterial = null;
                    IsCarrying = false;
                }

                // Check if agent has completed with the plan.
                if ( agentPlan.isFinished() ) {
                        WalkToPoint(currentLocation, startLocation);
                        carryingMaterial = null;
                        IsCarrying = false;
                        IsFinishedTask = true;
                        break;
                }


                // Get the new location from the knowledgeGraph base.
                GraphNode graphNodeNode = this.graph.findByPoint(currentLocation);
                // Check if the current(new) location is known. If i know the location then i know the neighborwood too.
                if (graphNodeNode == null) {
                    //logger.debug("Agent" + id + " - This location is not known" + currentLocation + ".");
                    graphNodeNode = addKnowledge(oldLocation, currentLocation);
                } else {
                    graphNodeNode.setVisitedTimes(graphNodeNode.getVisitedTimes() + 1);
                }

                // Check if there are any agents near me and exchange knowledgeGraph with them.
                if( !currentLocation.equals(startLocation)) {
                    List<AgentService> agentsNearList = getAgentsNear(currentLocation);
                    if (!agentsNearList.isEmpty()) {
                        //logger.debug("Agent" + id + " - Found" + agentsNearList.size() + " agent(s) near me.");
                        agentsNearList.stream().forEach((agent) -> ExchangeKnowledge(agent));
                    }
                }

                if( IsInTarget( currentLocation ) && !IsCarrying ){
                    if( IsAvailableToCarry( currentLocation )){
                        //See if target is known
                        GraphNode target = this.graph.findByPoint(currentLocation);
                        //add it to me target list
                        if( target == null ) {
                            this.graph.getNodeSet().add(target);
                            System.out.println("Added Target : " + target.getStadiumMaterials().getMaterialVocabulary() +
                                    " to GraphNode");
                        }
                        IsCarrying = true;
                        carryingMaterial = scenarioService.getStadiumEnvironment().getStadiumMaterialsOfPoint(currentLocation);
                    }
                    if( team.equals(Team.TEAM_B) && agentPlan.ShouldGoHome() ){
                        agentPlan.setFinished(true);
                    }
                    nextLocation = algorithPrvovider.getBFS().GetNextLocationHome(currentLocation, startLocation);
                    this.agentStatistics.setAgentMoveBfsCount(this.agentStatistics.getAgentMoveBfsCount() + 1);

                } else if( IsCarrying ){
                    nextLocation = algorithPrvovider.getBFS().GetNextLocationHome(currentLocation, startLocation);
                    this.agentStatistics.setAgentMoveBfsCount(this.agentStatistics.getAgentMoveBfsCount() + 1);
                } else {

                    // Check if i know the location of the target point of my plan.
                    if( team.equals(Team.TEAM_A) ){
                        GraphNode targetNode = this.graph.findByMaterial( this.agentPlan.getTarget() );
                        if( targetNode != null ){
                            System.out.println("Agent: " + this.toString() + "has finally knowledge of his target -> " + targetNode.getStadiumMaterials().getMaterialVocabulary());
                            
                            nextLocation = algorithPrvovider.getBFS().GetNextLocationHome(currentLocation, targetNode.getPoint());
                            this.agentStatistics.setAgentMoveBfsCount(this.agentStatistics.getAgentMoveBfsCount() + 1);
                        } else {
                            nextLocation = GetNextLocation();
                            this.agentStatistics.setAgentMoveLeastVisitedCount(this.agentStatistics.getAgentMoveLeastVisitedCount() + 1);
                        }
                    } else {
                        nextLocation = GetNextLocation();
                        this.agentStatistics.setAgentMoveLeastVisitedCount(this.agentStatistics.getAgentMoveLeastVisitedCount() + 1);
                    }
                }

                // Check the scenario state.
                checkScenarioState();

                // Move to the next location and update the current.
                WalkToPoint(currentLocation, nextLocation);
                oldLocation = currentLocation;
                currentLocation = nextLocation;

                // Sleep for some time to allow the other agents to work.
                sleepAgent();
            }
        } catch (Exception ex) {
            //logger.error(ex);
            ex.printStackTrace();
        }

        return super.call();

    }


    public GraphNode addKnowledge(GridPoint pointOld, GridPoint pointNew) {
        // The knowledgeGraph that will be created.
        GraphNode graphNode = new GraphNode();
        graphNode.setPoint(pointNew);
        graphNode.setVisitedTimes(1);

        // Add the new places of this point and its neighborwood.
        StadiumEnvironment environment = scenarioService.getStadiumEnvironment();
        graphNode.setStadiumMaterials(environment.getStadiumMaterialsOfPoint(pointNew));

        int height = Manager.getInstance().getEnvironment().getHeight(), width = Manager.getInstance().getEnvironment().getWidth();
        List<GridPoint> pointToSearchList = Validation.GenerateValidNeighbors(pointNew);
        List<GraphNode> graphNodes = new ArrayList<GraphNode>();

        List<StadiumMaterials> neighborMaterials = new ArrayList<>();
        for(GridPoint _gridPoint : pointToSearchList){
            //Fetch Material
            StadiumMaterials _material = environment.getStadiumMaterialsOfPoint(_gridPoint);
            if( _material != null ){
                //Βαλτα στην λίστα
                neighborMaterials.add(_material);
                //Create Children
                GraphNode _graphNode = new GraphNode();
                //Set Unique Location
                _graphNode.setPoint(_gridPoint);
                //Set material
                _graphNode.setStadiumMaterials(_material);
                _graphNode.setVisitedTimes(1);
                graphNodes.add(_graphNode);
                this.graph.getNodeSet().add(_graphNode);
            }
        }

        graphNode.setNeighborMaterials(neighborMaterials);
        graphNode.setChildrent(graphNodes);

        this.graph.getNodeSet().add(graphNode);
        this.graph.getUnique().add(graphNode);

        // Get the knoledge of the point before. In case of the point before is not existing in the graph (1st run), return.
        GraphNode NodeOld = this.graph.findByPoint(pointOld);
        if (NodeOld == null || NodeOld.equals(graphNode)) {
            return graphNode;
        }

        // In other cases connect with a line this knowledgeGraph with the one before.
        GraphLine line = new GraphLine(NodeOld, graphNode);
        this.graph.getLineSet().add(line);
        return graphNode;
    }


    private List<AgentService> getAgentsNear(GridPoint point) {
        List<AgentService> allAgentList = Manager.getInstance().getAgentServices();
        List<AgentService> agentNearList = new ArrayList<>(allAgentList.size());


        List<GridPoint> pointToSearchArray = Validation.GenerateValidNeighbors(point);

        final AgentService agent = this;
        allAgentList.stream().forEach(_agent -> {

            if( !_agent.equals(agent) && _agent.team == agent.team ){

                for (GridPoint location : pointToSearchArray) {
                    if (_agent.currentLocation.equals(location)) {
                        agentNearList.add(_agent);
                    }
                }
            }

        });
        return agentNearList;
    }


    private  synchronized boolean IsAvailableToCarry(GridPoint gridPoint){

        StadiumMaterials materials = scenarioService.getStadiumEnvironment().getStadiumMaterialsOfPoint(gridPoint);
        return this.agentPlan.IsAvailableForCarrying(materials);
    }
    private synchronized void ExchangeKnowledge(AgentService agentService){

        algorithPrvovider.getBFS().ExchangableKnowledgeWithAgent(this.graph,agentService);

    }

    public boolean IsInTarget(GridPoint gridPoint) {

        StadiumMaterials currentMaterial = scenarioService.getStadiumEnvironment().getStadiumMaterialsOfPoint(gridPoint);

        return currentMaterial != null ? this.agentPlan.ExistInTargets(currentMaterial) : false;
    }

    public synchronized void UpdateMaterialTarget(StadiumMaterials target) {


        int currentportion = this.agentPlan.getTargetsPortion().get(target.getMaterialVocabulary());

        if( currentportion > 0 )
            this.agentPlan.getTargetsPortion().replace(target.getMaterialVocabulary(), currentportion - 1);

        if( currentportion - 1 == 0 && this.agentPlan.getCollectionStrategy().equals(CollectionStrategy.SPECIFIC))
                this.agentPlan.setFinished(true);

    }

    public boolean isFinishedTask() {
        return IsFinishedTask;
    }

    public void setFinishedTask(boolean finishedTask) {
        IsFinishedTask = finishedTask;
    }
}
