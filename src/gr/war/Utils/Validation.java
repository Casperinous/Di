package gr.war.Utils;

import gr.war.Models.GraphModel.GraphNode;
import gr.war.Models.GridPoint;
import gr.war.Singletons.Manager;

import java.util.ArrayList;
import java.util.List;

public class Validation {

    public static boolean IsValidX(int x, int max){

        return x >= 0 && x <= max - 1 && x < Constants.MAX_WIDTH;

    }
    public static boolean IsValidY(int y, int max){

        return y >= 0 && y <= max - 1 && y < Constants.MAX_HEIGHT;

    }

    public static List<GridPoint> GenerateValidNeighbors(GridPoint point){

        int height = Manager.getInstance().getEnvironment().getHeight(), width = Manager.getInstance().getEnvironment().getWidth();
        List<GridPoint> pointToSearchList = new ArrayList<>();

        //Generate Neighbors
        if(Validation.IsValidX(point.getX(),width ) && Validation.IsValidY(point.getY() - 1, height))
            pointToSearchList.add( new GridPoint(point.getX(), point.getY() - 1));

        if(Validation.IsValidX(point.getX() + 1,width ) && Validation.IsValidY(point.getY(), height))
            pointToSearchList.add( new GridPoint(point.getX() + 1, point.getY()));

        if(Validation.IsValidX(point.getX(),width ) && Validation.IsValidY(point.getY() + 1, height))
            pointToSearchList.add( new GridPoint(point.getX(), point.getY() + 1));

        if(Validation.IsValidX(point.getX() - 1,width ) && Validation.IsValidY(point.getY(), height))
            pointToSearchList.add( new GridPoint(point.getX() - 1, point.getY()));

        return pointToSearchList;

    }
}
