package gr.war.Singletons;


import gr.war.Models.AgentStatistics;
import gr.war.Models.StadiumEnvironment;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StatisticManager {

    private static StatisticManager INSTANCE;

    private StatisticManager() {

    }

    public static StatisticManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatisticManager();
        }
        return INSTANCE;
    }

    public void export(List<AgentStatistics> agentStatisticses) {
        // Validate the location.

        String path = System.getProperty("user.home") + File.separator + "Documents";
        File folder = new File(path);
        if (!folder.isDirectory()) {
            System.out.println("Not valid location to save the statistics.");
            return;
        }
        
        // Append the date to the title of the statistic.
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); // File stamp.

        // Fill with content.
        try {
            StadiumEnvironment environment = Manager.getInstance().getEnvironment();
            //String title = statistic.getScenarioTitle();
            String format = dateFormat.format(date);
            File file = new File(folder, "Scenario_" + dateFormat.format(date));
            FileUtils.touch(file);

            // Write the environment information.
            FileUtils.writeStringToFile(file, getEnvironmentInfo(environment).toString() + "\n", StandardCharsets.UTF_8, true);

            // Write the agent's information.
            for (int i = 0; i < agentStatisticses.size(); i++) {
                FileUtils.writeStringToFile(file, getAgentInfo(agentStatisticses.get(i), i).toString() + "\n", StandardCharsets.UTF_8, true);
            }
        } catch (Exception ex) {
            return;
        }
    }


    private StringBuilder getEnvironmentInfo(StadiumEnvironment environment) {
        StringBuilder builder = new StringBuilder();
        builder.append("**************************************************").append("\n")
                .append("Στοιχεία Περιβάλλονος").append("\n")
                .append("[ - ] Διαστάσεις: ").append(environment.getHeight()).append("x").append(environment.getWidth()).append("\n")
                .append("[ - ] Πλήθος Πρακτόρων: ").append(Manager.getInstance().getAgentServices().size()).append("\n");

        return builder;
    }

    private StringBuilder getAgentInfo(AgentStatistics statistic,int index) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append("*************************************").append("\n")
                .append("Πράκτορας Νο").append(index + 1).append("\n")
                .append("[ - ] Ομάδα Πράκτορα").append(statistic.getTeam()).append("\n")
                .append("[ - ] Συνολικά βήματα εκτέλεσης: ").append(statistic.getAgentStepCount()).append("\n")
                .append("[ - ] Πλήθος ανταλλαγής γνώσεων: ").append(statistic.getAgentKnowledgeExchangeCount()).append("\n")
                .append("[ - ] Σύνολο κινήσεων με BFS: ").append(statistic.getAgentMoveBfsCount()).append("\n")
                .append("[ - ] Σύνολο κινήσεων με Least Visited: ").append(statistic.getAgentMoveLeastVisitedCount()).append("\n");

        return builder;
    }



}
