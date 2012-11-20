package br.ufpe.gprt.semantic;

import java.util.ArrayList;
import java.util.Map;

import br.ufpe.gprt.semantic.ContextManager.Enum_Action;

/**
 * It's the topic, or interest of the client or the middleware.
 * It has a policy list related to this context
 * @author GPRT-BEMO
 *
 */
public class Context {
	private String topic;
	private String description;
	private ArrayList<Policy> policies;
	private Map<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition> actions;
	
	public Context(){
		
	}
	public Context(String topic, String description, ArrayList<Policy> policies, Map<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition> actions)
	{
		this.topic = topic;
		this.description = description;
		this.policies = policies;
		this.actions = actions;
		
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
	public ArrayList<Policy> getPolicies() {
		return policies;
	}
	public void setPolicies(ArrayList<Policy> policies) {
		this.policies = policies;
	}
	
	public void addPolicy(Policy policy){
		if (!policies.contains(policy))
			this.policies.add(policy);
	}
	
	public boolean removePolicy(Policy policy){
		return this.policies.remove(policy);
	}


	public Map<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition> getActions(){
		return actions;
	}
	
}
