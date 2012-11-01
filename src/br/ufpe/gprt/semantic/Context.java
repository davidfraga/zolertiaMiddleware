package br.ufpe.gprt.semantic;

import java.util.List;

import br.ufpe.gprt.resources.ResourceManager;

/**
 * It's the topic, or interest of the client or the middleware.
 * It has a policy list related to this context
 * @author GPRT-BEMO
 *
 */
public class Context {
	private String topic;
	private String description;
	private List<Policy> policies;
	
	public Context(){
		
	}
	public Context(String topic, String description, List<Policy> policies)
	{
		this.topic = topic;
		this.description = description;
		this.policies = policies;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Policy> getPolicies() {
		return policies;
	}
	public void setPolicies(List<Policy> policies) {
		this.policies = policies;
	}
	
	public void addPolicy(Policy policy){
		if (!policies.contains(policy))
			this.policies.add(policy);
	}
	
	public boolean removePolicy(Policy policy){
		return this.policies.remove(policy);
	}
	public void activatePolicies() {
		for (Policy policy : this.policies) {
			ResourceManager.getInstance().getPolicyManager().policyStart(policy);
		}
	}
}
