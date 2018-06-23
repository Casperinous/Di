package gr.war.Models;

import gr.war.Singletons.Manager;
import gr.war.Utils.Constants;

import java.util.List;
import java.util.Map;


public class StadiumEnvironment {

    private String title;
    private int width;
    private int height;
    private Map<Integer, List<StadiumMaterials>> stadium;
    private List<StadiumMaterials> availableMaterials;
    private List<Village> villages;

    private StadiumEnvironment(StadiumEnviromentBuilder builder){

        title = builder.Title;
        width = builder.Width;
        height = builder.Height;
        stadium = builder.Stadium;
        villages = builder.Villages;
        availableMaterials = builder.AvailableMaterials;

    }
    private StadiumEnvironment(String title, int width, int height, Map<Integer, List<StadiumMaterials>> stadium, List<StadiumMaterials> availableMaterials) {
        title = title;
        width = width;
        height = height;
        stadium = stadium;
        availableMaterials = availableMaterials;
    }


    public StadiumMaterials getStadiumMaterialsOfPoint(GridPoint point) {
        // Validation.
        if (point.getX() < 0 || point.getX() >= Constants.MAX_HEIGHT || point.getX() > Manager.getInstance().getEnvironment().getWidth()) {
            return null;
        }
        if (point.getY() < 0 || point.getY() >= Constants.MAX_HEIGHT || point.getY() > Manager.getInstance().getEnvironment().getHeight()) {
            return null;
        }
        return stadium.get(point.getY()).get(point.getX());
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Map<Integer, List<StadiumMaterials>> getStadium() {
        return stadium;
    }

    public void setStadium(Map<Integer, List<StadiumMaterials>> stadium) {
        this.stadium = stadium;
    }

    public List<Village> getVillages() {
        return villages;
    }

    public void setVillages(List<Village> villages) {
        this.villages = villages;
    }

    public static class StadiumEnviromentBuilder {

        private String Title;
        private int Width;
        private int Height;
        private List<Village> Villages;
        private Map<Integer, List<StadiumMaterials>> Stadium;
        private List<StadiumMaterials> AvailableMaterials;

        public StadiumEnviromentBuilder() {
        }

        public StadiumEnviromentBuilder GridTitle(String title ){

            this.Title = title;
            return this;

        }
        public StadiumEnviromentBuilder GridWidth(int gridWidth ){

            this.Width = gridWidth;
            return this;

        }
        public StadiumEnviromentBuilder GridHeight(int gridHeight ){

            this.Height = gridHeight;
            return this;

        }

        public  StadiumEnviromentBuilder Villages(List<Village> villages){

            this.Villages = villages;
            return this;
        }

        public StadiumEnviromentBuilder Stadium(Map<Integer, List<StadiumMaterials>> stadium ){

            this.Stadium = stadium;
            return this;

        }

        public StadiumEnviromentBuilder AvailableMaterials(List<StadiumMaterials> availableMaterials){

            this.AvailableMaterials = availableMaterials;
            return this;

        }

        public StadiumEnvironment build() {

            return new StadiumEnvironment(this);

        }



    }







}
