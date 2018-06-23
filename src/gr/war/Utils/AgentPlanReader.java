package gr.war.Utils;

import gr.war.CustomExceptions.MultipleMaterialException;
import gr.war.CustomExceptions.MultipleVillagesException;
import gr.war.CustomExceptions.UnkownVillageException;
import gr.war.Models.StadiumMaterials;
import gr.war.Models.Village;
import gr.war.Singletons.Manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AgentPlanReader {

    public AgentPlanReader() throws Exception {
        throw new Exception("Specify a file object");
    }


    public AgentPlanReader(File file) throws Exception {

        if( Manager.getInstance().getVillageA() == null || Manager.getInstance().getVillageB() == null )
            throw new Exception("First you gotta init the grid man.");

        if (file == null)
            throw new IOException("File is null ");

        readFile(file);

    }

    public void readFile(File file) throws IOException {

        Village villageA = Manager.getInstance().getVillageA(),
                villageB = Manager.getInstance().getVillageB();

        try {
            Files.readAllLines(file.toPath()).forEach(s -> {

                String[] tokens = s.split(":");
                if( tokens.length == 3){

                    //Scan for the Village
                    char village  = tokens[0].charAt(0);
                    //Try to match first char
                    StadiumMaterials mVillage = StadiumMaterials.matchFromMaterialVocabulary(village);
                    // If we found a match and it is a village one
                    if( mVillage != null && ( mVillage.equals(StadiumMaterials.VILLAGEA) || mVillage.equals(StadiumMaterials.VILLAGEB) )) {
                        //Get Material Char
                        char material = tokens[1].charAt(0);
                        //Try to find a Match
                        StadiumMaterials mMaterial = StadiumMaterials.matchFromMaterialVocabulary(material);
                        //Check if you succeed
                        if (mMaterial != null) {
                            //Get the desired portion
                            int portion = Integer.parseInt(tokens[2]);
                            //If it is valid
                            if (portion > 0) {
                                //Insert it to the villages
                                if (mVillage.equals(StadiumMaterials.VILLAGEA) && villageA.getTargetsPortion().get(mMaterial) == null) {
                                    villageA.getTargetsPortion().put(mMaterial.getMaterialVocabulary(), portion);
                                    villageA.getTargets().add(mMaterial);
                                } else if (mVillage.equals(StadiumMaterials.VILLAGEB) && villageB.getTargetsPortion().get(mMaterial) == null) {
                                    villageB.getTargetsPortion().put(mMaterial.getMaterialVocabulary(), portion);
                                    villageB.getTargets().add(mMaterial);
                                } else {
                                    try {
                                        throw new MultipleMaterialException("Found either multiple Entries of the same Material" + mMaterial.getMaterialVocabulary());
                                    } catch (MultipleMaterialException e) {
                                        e.toString();
                                    }
                                }
                            } else {
                                System.out.println("Invalid Number");
                            }
                        } else {
                            System.out.println("Could not recognise material character");
                        }
                    } else {

                        try {
                            throw new UnkownVillageException("First Character could not match to a village");
                        } catch (UnkownVillageException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
