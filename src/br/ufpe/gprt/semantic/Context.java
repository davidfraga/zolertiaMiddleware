package br.ufpe.gprt.semantic;

import java.util.ArrayList;
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
	private ArrayList<Policy> policies;
	private PolicyManager.Enum_Action s_actionIfInContext;
	private PolicyManager.Enum_Action s_actionIfOutContext;
	
	public Context(){
		
	}
	public Context(String topic, String description, ArrayList<Policy> policies, PolicyManager.Enum_Action actionCode1, PolicyManager.Enum_Action actionCode2)
	{
		this.topic = topic;
		this.description = description;
		this.policies = policies;
		s_actionIfInContext = actionCode1;
		s_actionIfOutContext = actionCode2;
		
	}
	public String getTopic() {
		return topic;
	}
	
	public PolicyManager.Enum_Action getActionIn () {
		return s_actionIfInContext;
	}
	
	public PolicyManager.Enum_Action getActionOut () {
		return s_actionIfOutContext;
	}
	
	public void setActionIn (PolicyManager.Enum_Action actionIn){
		s_actionIfInContext = actionIn;
	}
	
	public void setActionOut (PolicyManager.Enum_Action actionOut){
		s_actionIfOutContext = actionOut;
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
	/*public void activatePolicies() {
		for (Policy policy : this.policies) {
			ResourceManager.getInstance().getPolicyManager().policyStart(policy);
		}
	}*/
}
