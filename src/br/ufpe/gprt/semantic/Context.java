package br.ufpe.gprt.semantic;

import java.util.List;

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
}
