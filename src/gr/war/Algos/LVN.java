package gr.war.Algos;

import gr.war.Models.GraphModel.Graph;
import gr.war.Models.GraphModel.GraphNode;
import gr.war.Models.GridPoint;
import gr.war.Models.StadiumMaterials;
import gr.war.Singletons.Manager;
import gr.war.Utils.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class LVN {


    public GridPoint GetNextLocation(GridPoint point, Graph graph){

        List<StadiumMaterials> Materials = new ArrayList<>();


        int height = Manager.getInstance().getEnvironment().getHeight(), width = Manager.getInstance().getEnvironment().getWidth();
        List<GridPoint> pointToSearchList = new ArrayList<>();

        if(Validation.IsValidX(point.getX(),width ) && Validation.IsValidY(point.getY() - 1, height))
            pointToSearchList.add( new GridPoint(point.getX(), point.getY() - 1));

        if(Validation.IsValidX(point.getX() + 1,width ) && Validation.IsValidY(point.getY(), height))
            pointToSearchList.add( new GridPoint(point.getX() + 1, point.getY()));

        if(Validation.IsValidX(point.getX(),width ) && Validation.IsValidY(point.getY() + 1, height))
            pointToSearchList.add( new GridPoint(point.getX(), point.getY() + 1));

        if(Validation.IsValidX(point.getX() - 1,width ) && Validation.IsValidY(point.getY(), height))
            pointToSearchList.add( new GridPoint(point.getX() - 1, point.getY()));



        GridPoint[] pointToSearchArray = pointToSearchList.toArray(new GridPoint[pointToSearchList.size()]);

        //Gl


        // Select the one with the min visited times.
        int visitedTimesMin = Integer.MAX_VALUE;

        GridPoint nextLocation = null;
        Shuffle(pointToSearchArray);

        for (GridPoint currentPoint : pointToSearchArray) {
            // Check the graph if contains this point (re-visited point).
            GraphNode node = graph.findByPoint(currentPoint);

            // If it is not revisited id prefered. In other case check the visited times.
            if (node == null) {
                nextLocation = currentPoint;
                break;
            } else if (node.getVisitedTimes() <= visitedTimesMin) {
                nextLocation = node.getPoint();
                visitedTimesMin = node.getVisitedTimes();
            }
        }

        // In case of an agent house that is not near to a road.
        if (nextLocation == null) {
            throw new NullPointerException("This location is not including a road near.");
        }

        return nextLocation;
    }

    private void Shuffle(GridPoint[] gridPoints){

        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = gridPoints.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            GridPoint a = gridPoints[index];
            gridPoints[index] = gridPoints[i];
            gridPoints[i] = a;
        }

    }


}
