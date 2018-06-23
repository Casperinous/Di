package gr.war.Algos;

import gr.war.Models.GraphModel.GraphNode;

/**
 * Created by nikostot on 22/7/2016.
 */
public final class AlgorithPrvovider {

    private BFS BFSInstance;
    private LVN LVNInstance;

    private int width;
    private int height;
    private GraphNode root;

    private AlgorithPrvovider(int width, int height, GraphNode root){
        this.width = width;
        this.height = height;
        this.root = root;
    };

    public static AlgorithPrvovider create(int width, int height, GraphNode root){

        return new AlgorithPrvovider(width,height, root);

    }

    public BFS getBFS(){

        if( BFSInstance == null )
            BFSInstance = new BFS(width,height, root);

        return BFSInstance;
    }

    public LVN getLVN(){

        if( LVNInstance == null )
            LVNInstance = new LVN();

        return LVNInstance;

    }

}
