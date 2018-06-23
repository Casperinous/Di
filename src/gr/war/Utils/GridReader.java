package gr.war.Utils;

import gr.war.CustomExceptions.MultipleVillagesException;
import gr.war.Models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridReader {

    private String Title = "";
    private int GridRows = 0;
    private int GridColumns = 0;
    /*
    private int AgentsA = 0;
    private int AgentsB = 0;
    */
    private Village villageA, villageB;
    private Map<Integer, List<StadiumMaterials>> Stadium;
    private List<StadiumMaterials> AvailableMaterials;

    public GridReader() throws Exception {

        throw new Exception("Specify a file object");
    }

    public GridReader(File file) throws IOException, MultipleVillagesException {

        if( file == null )
            throw new IOException("File is null ");

        try {
            readFile(file);
        } catch (IOException ex){
            System.out.println(ex.toString());
        }

    }

    public StadiumEnvironment GenerateStadiumEnviroment(){

        List<Village> villages = new ArrayList<>();
        villages.add(villageA);
        villages.add(villageB);

         return new  StadiumEnvironment
                 .StadiumEnviromentBuilder()
                 .GridTitle(Title)
                 .GridHeight(GridRows)
                 .GridWidth(GridColumns)
                 .Villages(villages)
                 .Stadium(Stadium)
                 .AvailableMaterials(AvailableMaterials)
                 .build();
    }

    private void readFile(File file) throws IOException, MultipleVillagesException{

        try {

                Title = file.getName();
                Stadium = new HashMap<>();
                AvailableMaterials = new ArrayList<>();
                List<String> lines = Files.readAllLines(file.toPath());
                for( int x = 0, y = lines.size(); x < y; x++){

                    String s = lines.get(x);

                    if( GridColumns < s.length())
                        GridColumns = s.length();

                    List<StadiumMaterials> Materials = new ArrayList<StadiumMaterials>(GridColumns);

                    if( s.length() > 0 && s.length() == GridColumns) {
                        for (int i = 0; i < s.length(); i++) {

                            char chr = s.charAt(i);

                            StadiumMaterials material = StadiumMaterials.matchFromMaterialVocabulary(chr);

                            if (!IsNotVillage(material)) {
                                InitializeVillage(material,i, x);

                            }
                            AddInAvailabe(material);
                            //CheckForVillages(material);
                            if (material != null) Materials.add(material);
                        }

                        Stadium.put(GridRows, Materials);

                        GridRows++;
                    }


                }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private boolean IsNotVillage(StadiumMaterials stadiumMaterials){

        return !stadiumMaterials.equals(StadiumMaterials.VILLAGEA) && !stadiumMaterials.equals(StadiumMaterials.VILLAGEB);
    }


    private void InitializeVillage(StadiumMaterials material, int x, int y){

        if( material.equals(StadiumMaterials.VILLAGEA ) )
            if( villageA == null) {
                villageA = Village.create();
                villageA.setTeam(Team.TEAM_A);
                villageA.setStartlocation(new GridPoint(x,y));
            }
            else
                try {
                    throw new MultipleVillagesException("Multiple Villages found");
                } catch (MultipleVillagesException e) {
                    e.toString();
                }

        if( material.equals(StadiumMaterials.VILLAGEB ) )
            if( villageB == null) {
                villageB = Village.create();
                villageB.setTeam(Team.TEAM_B);
                villageB.setStartlocation(new GridPoint(x,y));
            }
            else
                try {
                    throw new MultipleVillagesException("Multiple Villages found");
                } catch (MultipleVillagesException e) {
                    e.toString();
                }

    }
    
    private void AddInAvailabe(StadiumMaterials stadiumMaterials){


        AvailableMaterials.stream().forEach(_stadiumMaterials -> {
            if( !stadiumMaterials.equals(StadiumMaterials.TERRAIN) && !_stadiumMaterials.equals(stadiumMaterials))
                AvailableMaterials.add(stadiumMaterials);
        });

    }





}
