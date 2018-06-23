package gr.war.Models;

public enum StadiumMaterials {

    AGENTA("AgentA.jpg","AgentA"),
    AGENTB("AgentB.png","AgentB"),
    TERRAIN("Terrain.png","Terrain"),
    VILLAGEA("VillageA.png", "VillageA"),
    VILLAGEB("VillageB.png", "VillageB"),
    HOUSE("House.png", "House"),
    STREET("Street.png", "Street"),
    WOOD("Wood.png","Wood"),
    MEAT("Meat.png","Meat"),
    STONE("Stone.png","Stone"),
    FRUIT("Fruit.png","Fruit"),
    GOLD("Gold.png","Gold");


    private StadiumMaterials(String MaterialName, String MaterialVocabulary) {

        this.MaterialName = MaterialName;
        this.MaterialVocabulary = MaterialVocabulary;
    }

    private String MaterialName;
    private String MaterialVocabulary;

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getMaterialVocabulary() {
        return MaterialVocabulary;
    }

    public void setMaterialVocabulary(String materialVocabulary) {
        MaterialVocabulary = materialVocabulary;
    }



    public static StadiumMaterials matchFromMaterialVocabulary(char MaterialVocabulary){

        switch (MaterialVocabulary) {
            case 'W':
                return WOOD;
            case 'M':
                return MEAT;
            case 'S':
                return STONE;
            case 'F':
                return FRUIT;
            case 'G':
                return GOLD;
            case 'H':
                return HOUSE;
            case  '*':
                return TERRAIN;
            case  '1':
                return VILLAGEA;
            case  '2':
                return VILLAGEB;
            default:
                return null;
        }
    }



}
