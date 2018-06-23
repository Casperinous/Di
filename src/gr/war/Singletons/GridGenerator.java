package gr.war.Singletons;

import gr.war.Models.GridImgView;
import gr.war.Models.StadiumMaterials;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public final class GridGenerator {

    private static GridGenerator Instance = null;


    public static GridGenerator getInstance(){

        if( Instance == null )
            Instance = new GridGenerator();

        return Instance;
    }


    public void InitalizeGridTerrain(GridPane gridPane, int width, int height){

        gridPane.gridLinesVisibleProperty().set(true);
        Image image = new Image(getClass().getResourceAsStream("/RFiles/Imgs/Stadium/" + StadiumMaterials.TERRAIN.getMaterialName()));
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                GridImgView imageView = new GridImgView(image);
                imageView.setMaterial(StadiumMaterials.TERRAIN.getMaterialVocabulary());
                imageView.setFitWidth(40);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);
                // Drag utiliti.
                // Add it to grid.
                gridPane.add(imageView, col, row);
            }
        }


    }

    public void InsertMaterials(GridPane gridPane, Map<Integer, List<StadiumMaterials>> Stadium){

        for( int i = 0, y = Stadium.size(); i < y; i++) {

            List<StadiumMaterials> row = Stadium.get(i);
            for (int x = 0, t = row.size(); x < t; x++) {

                StadiumMaterials material = row.get(x);

                if (!material.equals(StadiumMaterials.TERRAIN)) {
                    Image image = new Image(getClass().getResourceAsStream("/RFiles/Imgs/Stadium/" + material.getMaterialName()));
                    if (image != null) {
                        GridImgView imageView = new GridImgView(image);
                        imageView.setMaterial(material.getMaterialVocabulary());
                        imageView.setFitWidth(40);
                        imageView.setPreserveRatio(true);
                        imageView.setSmooth(true);
                        imageView.setCache(true);
                        gridPane.add(imageView, x, i);
                    }
                }


            }
        }
    }


}
