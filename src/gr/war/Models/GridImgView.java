package gr.war.Models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class GridImgView extends ImageView {

    public String Material = "";

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public GridImgView(Image image){
        super(image);
        this.setPreserveRatio(true);
        this.setSmooth(true);
        this.setCache(true);
    }


}


