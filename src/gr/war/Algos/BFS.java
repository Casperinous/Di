package gr.war.Algos;

import gr.war.Models.ExchangableKnowledge;
import gr.war.Models.GraphModel.Graph;
import gr.war.Models.GraphModel.GraphLine;
import gr.war.Models.GraphModel.GraphNode;
import gr.war.Models.GridPoint;
import gr.war.Services.AgentService;
import gr.war.Utils.Constants;
import gr.war.Utils.Validation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nikostot on 22/7/2016.
 */
public class BFS extends ExchangableKnowledge {

    private  List<GraphNode> ClosedNodeSet;
    private  List<GraphNode> OpenNodeSet;
    private  Set<GraphLine> lineSet;
    private  int idx = 0;

    private  final int Width;
    private  final int Height;
    private  final GraphNode Root;


    public BFS(int width, int height, GraphNode root) {
        ClosedNodeSet = new LinkedList<>();
        OpenNodeSet = new LinkedList<>();
        lineSet = new HashSet<>();
        Width = width;
        Height = height;
        Root = root;
        this.InitGraph();
    }

    private List<GraphNode> GenerateNeigborsPositionsPositive(GridPoint currentLocation){

        List<GraphNode> Nodes = new LinkedList<>();



        if( Validation.IsValidY(currentLocation.getY() + 1, Height) ) {
            GridPoint npoint = new GridPoint(currentLocation.getX(), currentLocation.getY() + 1);
            Nodes.add(new GraphNode(npoint, 0));
        }

        if( Validation.IsValidX( currentLocation.getX() + 1 ,Width ) ){
            GridPoint npoint = new GridPoint(currentLocation.getX() + 1, currentLocation.getY());
            Nodes.add(new GraphNode(npoint, 0));
        }

        if( Validation.IsValidX( currentLocation.getX() + - 1,Width ) ){
            GridPoint npoint = new GridPoint(currentLocation.getX() - 1, currentLocation.getY());
            Nodes.add(new GraphNode(npoint, 0));
        }

        return Nodes;

    }

    private List<GraphNode> GenerateNeigborsPositionsNegative(GridPoint currentLocation){

        List<GraphNode> Nodes = new LinkedList<>();

        if( currentLocation.getY() - 1 >= 0 ) {
            GridPoint npoint = new GridPoint(currentLocation.getX(), currentLocation.getY() - 1);
            Nodes.add(new GraphNode(npoint, 0));
        }

        if( currentLocation.getX() - 1 >= 0 ){
            GridPoint npoint = new GridPoint(currentLocation.getX() - 1, currentLocation.getY());
            Nodes.add(new GraphNode(npoint, 0));
        }

        if( currentLocation.getX() - 1 >= 0 && currentLocation.getY() - 1 <= 0){
            GridPoint npoint = new GridPoint(currentLocation.getX() - 1, currentLocation.getY() - 1);
            Nodes.add(new GraphNode(npoint, 0));
        }

        return Nodes;

    }


    private List<GraphNode> GenerateNeigborsPositions(GridPoint currentLocation){

        List<GraphNode> Nodes = new LinkedList<>();

        if ( currentLocation.getX() == Constants.MAX_WIDTH || currentLocation.getY() == Constants.MAX_HEIGHT)
            Nodes = GenerateNeigborsPositionsNegative(currentLocation);

        if( currentLocation.getX() == 0 || currentLocation.getY() == 0)
            Nodes = GenerateNeigborsPositionsPositive(currentLocation);

        return Nodes;

    }

    public void InitGraph(){

        ClosedNodeSet.add(Root);
        this.OpenNodeSet = GenerateNeigborsPositions(Root.getPoint());
    }

    public GridPoint GetNextLocation(){

        GridPoint nextLocation = null;

        if( OpenNodeSet.size() != 0 ){

            GraphNode first = OpenNodeSet.get(0);
            OpenNodeSet.remove(0);
            ClosedNodeSet.add(first);
            nextLocation = first.getPoint();
            idx++;

        } else {

            OpenNodeSet = GenerateNeigborsPositions( ClosedNodeSet.get(idx).getPoint());
            nextLocation = GetNextLocation();
        }

        return nextLocation;

    }

    public GridPoint GetNextLocationHome(GridPoint currentPoint, GridPoint homePoint){

        GridPoint nextLocation = new GridPoint(currentPoint.getX(),currentPoint.getY());

        if( currentPoint.getX() != homePoint.getX() ) {
            if (currentPoint.getX() > homePoint.getX()) {
                nextLocation.setX(currentPoint.getX() - 1);
                return nextLocation;
            } else {
                nextLocation.setX(currentPoint.getX() + 1);
                return nextLocation;
            }
        } else {

            if( currentPoint.getY() != homePoint.getY()) {
                if (currentPoint.getY() > homePoint.getY())
                    nextLocation.setY(currentPoint.getY() - 1);
                else
                    nextLocation.setY(currentPoint.getY() + 1);
            }

            return nextLocation;
        }


    }

    public void ExchangableKnowledgeWithAgent(Graph from,AgentService agentService){

        Graph agentGraph = agentService.getGraph();

        List<GraphNode> clonedAgentNodeList = new ArrayList<>();
        List<GraphNode> clonedAgentUniqueList = new ArrayList<>();

        if( agentGraph.getNodeSet() != null && agentGraph.getUnique() != null  ) {
            clonedAgentNodeList.addAll(agentGraph.getNodeSet().stream().filter(graphNode -> from.findByPoint(graphNode.getPoint()) == null).collect(Collectors.toList()));
            clonedAgentUniqueList.addAll(agentGraph.getUnique().stream().filter(graphNode -> from.findByUniqueSet(graphNode.getPoint()) == null).collect(Collectors.toList()));
        }

        from.getNodeSet().addAll(clonedAgentNodeList);
        from.getUnique().addAll(clonedAgentUniqueList);

        agentService.getAgentStatistics().setAgentKnowledgeExchangeCount(agentService.getAgentStatistics().getAgentKnowledgeExchangeCount() + 1);

        return;
    }


}
