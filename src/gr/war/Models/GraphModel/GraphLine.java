package gr.war.Models.GraphModel;

public class GraphLine {

    private final GraphNode source;
    private final GraphNode destination;
    private final int weight = 1; // I am not using weights.

    public GraphLine(GraphNode source, GraphNode destination) {
        this.source = source;
        this.destination = destination;
    }

    public GraphLine(GraphLine item) {
        this.source = new GraphNode(item.getSource());
        this.destination = new GraphNode(item.getDestination());
    }

    public GraphNode getDestination() {
        return destination;
    }

    public GraphNode getSource() {
        return source;
    }

    public int getWeight() {
        return weight;
    }

    
}
