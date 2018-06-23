package gr.war.Controllers;

import gr.war.CustomExceptions.MultipleVillagesException;
import gr.war.Models.*;
import gr.war.Services.AgentService;
import gr.war.Services.ScenarioService;
import gr.war.Singletons.GridGenerator;
import gr.war.Singletons.Manager;
import gr.war.Utils.AgentPlanReader;
import gr.war.Utils.GridReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainViewSceneController implements Initializable {

    @FXML
    ScrollPane GridContainer;

    @FXML
    MenuItem GridLoader;

    @FXML
    MenuItem MaterialLoader;

    @FXML
    Button StartBtn;

    @FXML
    Button PauseBtn;
    @FXML
    Button StopBtn;
    @FXML
    Button RestartBtn;

    private Stage getStage() {
        return (Stage) GridContainer.getScene().getWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        GridLoader.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt"));

                Stage stage = getStage();

                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {

                    try {
                        GridReader reader = new GridReader(selectedFile);
                        StadiumEnvironment env = reader.GenerateStadiumEnviroment();
                        GridPane gridPane = new GridPane();
                        GridGenerator.getInstance().InitalizeGridTerrain(gridPane, env.getWidth(), env.getHeight());
                        GridGenerator.getInstance().InsertMaterials(gridPane, env.getStadium());
                        GridContainer.setContent(gridPane);
                        Manager.getInstance().setEnvironment(env);
                        Manager.getInstance().InitVillages();
                        Manager.getInstance().setGrid(gridPane);

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (MultipleVillagesException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        MaterialLoader.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt"));

                Stage stage = getStage();

                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {

                    try {

                        AgentPlanReader reader = new AgentPlanReader(selectedFile);

                        ScenarioService scenario = new ScenarioService.ScenarioBuilder()
                                .title("Agent Hunting")
                                .StadiumEnvironment(Manager.getInstance().getEnvironment())
                                .build();
                        scenario.setScenarioStateProperty(ScenarioState.READY);
                        Manager.getInstance().setScenarioService(scenario);
                        //Test initialize a game
                        Manager.getInstance().InitComponents();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (MultipleVillagesException e1) {
                        e1.printStackTrace();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


        StartBtn.setOnAction(event -> {
            ScenarioService scenarioService = Manager.getInstance().getScenarioService();
            if (scenarioService != null) {
                scenarioService.play();
            }

        });

        PauseBtn.setOnAction(event -> {
            ScenarioService scenarioService = Manager.getInstance().getScenarioService();
            if (scenarioService != null) {
                scenarioService.pause();
            }

        });

        PauseBtn.setOnAction(event -> {
            ScenarioService scenarioService = Manager.getInstance().getScenarioService();
            if (scenarioService != null) {
                scenarioService.pause();
            }

        });

        RestartBtn.setOnAction(event -> {
            ScenarioService scenarioService = Manager.getInstance().getScenarioService();
            if (scenarioService != null) {
                scenarioService.restart();
            }

        });

        StopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScenarioService scenarioService = Manager.getInstance().getScenarioService();
                if (scenarioService != null) {
                    scenarioService.stopPlaying();
                }

            }
        });


    }
}
