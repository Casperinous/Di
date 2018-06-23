package gr.war.Services;

import gr.war.Models.Agent;
import gr.war.Models.AgentStatistics;
import gr.war.Models.ScenarioState;
import gr.war.Models.StadiumEnvironment;
import gr.war.Singletons.GridGenerator;
import gr.war.Singletons.Manager;
import gr.war.Singletons.StatisticManager;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;


import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioService extends Task<Object> {

    private final String Title;
    private final StadiumEnvironment stadiumEnvironment;
    private final ObjectProperty<ScenarioState> scenarioStateProperty ;
    private int durationSeconds;

    public ScenarioState getScenarioState() {
        return scenarioStateProperty.get();
    }

    public ScenarioService(ScenarioBuilder builder) {

        Title = builder.title;
        stadiumEnvironment = builder.stadiumEnvironment;
        scenarioStateProperty = new SimpleObjectProperty<>(ScenarioState.NOT_READY);
    }

    @Override
    protected Object call() throws Exception {
        while (true) {
            Thread.sleep(1000);

            if (getScenarioState() == ScenarioState.RUNNING) {
                // Check if all agent are completed.
                //logger.debug("Scenar - Check if all agent are completed.");

                int agentCompleted = 0;
                for (AgentService agent : Manager.getInstance().getAgentServices()) {
                    if( agent.isFinishedTask())
                        agentCompleted++;
                }

                if( agentCompleted == Manager.getInstance().getAgentServices().size() ){
                    scenarioCompleted();
                    break;
                }

                // Update the duration seconds.
                //logger.debug("Scenar - Update the duration seconds to " + durationSeconds + ".");
                durationSeconds++;

                // Update the label.
                Platform.runLater(() -> {
                    //MasterManager.getInstance().getDurationSecondsLabel().setText(String.format("%02d:%02d:%02d", durationSeconds / 3600, (durationSeconds % 3600) / 60, (durationSeconds % 60)));
                });
            }
        }
        return null;
    }

    public StadiumEnvironment getStadiumEnvironment() {
        return stadiumEnvironment;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public ScenarioState getScenarioStateProperty() {
        return scenarioStateProperty.get();
    }

    public ObjectProperty<ScenarioState> scenarioStatePropertyProperty() {
        return scenarioStateProperty;
    }

    public void setScenarioStateProperty(ScenarioState scenarioStateProperty) {
        this.scenarioStateProperty.set(scenarioStateProperty);
    }

    public static class ScenarioBuilder {

        private String title;
        private StadiumEnvironment stadiumEnvironment;
        private ObjectProperty<ScenarioState> scenarioStateProperty;
        private int durationSeconds;

        public ScenarioBuilder() {
            this.title = "";
            this.stadiumEnvironment = null;
            this.durationSeconds = 0;
        }

        public ScenarioBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ScenarioBuilder StadiumEnvironment(StadiumEnvironment stadiumEnvironment) {
            this.stadiumEnvironment = stadiumEnvironment;
            return this;
        }

        public ScenarioBuilder ScenarioStateProperty(ObjectProperty<ScenarioState> scenarioStateProperty) {
            this.scenarioStateProperty = scenarioStateProperty;
            return this;
        }

        public ScenarioBuilder durationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }

        public ScenarioService build() {
            return new ScenarioService(this);
        }

    }



    private void scenarioCompleted() {

        List<AgentStatistics> agentStatisticses = Manager.getInstance().getAgentServices().stream().map(Agent::getAgentStatistics).collect(Collectors.toList());

        StatisticManager.getInstance().export(agentStatisticses);

        // Terminate scenario.
        Platform.runLater(() -> {
            // Upadte the state to stopped.
            setScenarioStateProperty(ScenarioState.STOPPED);


            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Η συλλογή τελείωσε", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                alert.hide();
            }

        });
    }


    public void play() {
        setScenarioStateProperty(ScenarioState.RUNNING);
    }

    public void pause() {
        setScenarioStateProperty(ScenarioState.PAUSED);
    }

    public void stopPlaying() {
        // This state will cancel all the agent tasks that are running.
        setScenarioStateProperty(ScenarioState.STOPPED);
    }

    public void restart() {
        setScenarioStateProperty(ScenarioState.STOPPED);




        // Create a new scenario with the same characteristics of this.
        ScenarioService scenario = new ScenarioService.ScenarioBuilder()
                .title("")
                .StadiumEnvironment(stadiumEnvironment)
                .build();
        scenario.setScenarioStateProperty(ScenarioState.READY);
        Manager.getInstance().setScenarioService(scenario);

        // Create the agents.

        Platform.runLater(() -> {
            GridGenerator.getInstance().InsertMaterials(Manager.getInstance().getGrid(), stadiumEnvironment.getStadium());
            Manager.getInstance().InitComponents();
            Manager.getInstance().getScenarioService().play();
        });

    }
}
