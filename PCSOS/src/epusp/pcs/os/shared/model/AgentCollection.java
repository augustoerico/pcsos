package epusp.pcs.os.shared.model;

import java.util.ArrayList;
import java.util.List;

import epusp.pcs.os.shared.model.person.user.Agent;

public class AgentCollection {
	List<Agent> agents;
	
	public AgentCollection() {
        this.agents = new ArrayList<Agent>();
    }

    public AgentCollection(List<Agent> agents) {
        this.agents = agents;
    }

    public List<Agent> getAgentCollection() {
        return agents;
    }

    public void setAgentCollection(List<Agent> agents) {
        this.agents = agents;
    }
}
