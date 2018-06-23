package gr.war.Algos;

import gr.war.Models.GraphModel.Graph;
import gr.war.Models.GraphModel.GraphNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch {

    private GraphNode source;
    private GraphNode target;
    private Queue<GraphNode> queue;
    private ArrayList<GraphNode> explored;

    private BreadthFirstSearch(GraphNode source, GraphNode target){
        this.source = source;
        this.target = target;
        queue = new LinkedList<>();
        explored = new ArrayList<>();
    }

    public static BreadthFirstSearch create(GraphNode source, GraphNode target){

        return new BreadthFirstSearch(source, target);
    }

    public boolean compute(Graph graph){

        if(this.source.equals(target)){
            System.out.println("Goal Node Found!");
        }

        queue.add(this.source);
        explored.add(source);
        while(!queue.isEmpty()){

            GraphNode current = queue.remove();

            if(current.equals(this.target)) {
                System.out.println(explored);
                return true;
            }  else {

                if(current.getChildrent() == null || current.getChildrent().isEmpty()) {
                    current = graph.findByUniqueSet(current.getPoint());
                    if (current != null && current.getChildrent() != null )
                        queue.addAll(current.getChildrent());
                }
                else
                    queue.addAll(current.getChildrent());
            }
            explored.add(current);
        }
        return false;
    }

    public ArrayList<GraphNode> GetCorrectPath(){
        return explored;
    }
}
