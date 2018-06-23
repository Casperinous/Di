package gr.war.Models;

public class AgentStatistics {

    private int AgentStepCount = 0;
    private int AgentKnowledgeExchangeCount = 0;
    private int AgentMoveBfsCount = 0;
    private int AgentMoveLeastVisitedCount = 0;
    private Team team;

    public int getAgentStepCount() {
        return AgentStepCount;
    }

    public void setAgentStepCount(int agentStepCount) {
        AgentStepCount = agentStepCount;
    }

    public int getAgentKnowledgeExchangeCount() {
        return AgentKnowledgeExchangeCount;
    }

    public void setAgentKnowledgeExchangeCount(int agentKnowledgeExchangeCount) {
        AgentKnowledgeExchangeCount = agentKnowledgeExchangeCount;
    }

    public int getAgentMoveBfsCount() {
        return AgentMoveBfsCount;
    }

    public void setAgentMoveBfsCount(int agentMoveBfsCount) {
        AgentMoveBfsCount = agentMoveBfsCount;
    }

    public int getAgentMoveLeastVisitedCount() {
        return AgentMoveLeastVisitedCount;
    }

    public void setAgentMoveLeastVisitedCount(int agentMoveLeastVisitedCount) {
        AgentMoveLeastVisitedCount = agentMoveLeastVisitedCount;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
