package br.ufpe.gprt.semantic;
import java.util.ArrayList;
import java.util.Vector;

import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.semantic.PolicyManager.Enum_Action;
import br.ufpe.gprt.semantic.PolicyManager.Enum_Condition;
import br.ufpe.gprt.semantic.PolicyManager.Enum_DataType;

/**
 * Manages the context available. It has predefined contexts and active contexts
 * @author GPRT-BEMO
 *
 */
public class ContextManager {

	private Vector<Context> predefinedContexts;
	private Vector<Context> activeContexts;

	public ContextManager() {

		//s_policyManager = new PolicyManager();
		predefinedContexts = new Vector<Context>();
		activeContexts = new Vector<Context>();
		fillContexts();
	}
	
	public Vector<Context> getPredefinedContexts(){
		return predefinedContexts;
	}
	
	/**
	 * Create predefined contexts
	 */
	private void fillContexts() {
		// Human Body Context Construction
		String topic = "human_body_temperature";
		String description = "the human body temperature should not be less than 35ºC or greater than 40ºC";
		Policy h1_policy = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.LESS_THAN, 35, Enum_Action.SEND_PACKETS_MORE_FREQUENTLY);
		Policy h2_policy = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.GREATER_THAN, 40, Enum_Action.SEND_PACKETS_MORE_FREQUENTLY);
		
		Vector<Policy> policies = new Vector<Policy>();
		policies.add(h1_policy);
		policies.add(h2_policy);
		Context humanbody = new Context(topic, description, policies);
		
		predefinedContexts.add(humanbody);
		// End of Human Body Context Construction

	}
	
	public void insertActiveContext(Context context){
		if (!this.activeContexts.contains(context))
			this.activeContexts.add(context);
		context.activatePolicies();
	}
	
	public void removeActiveContext(Context context){
		this.activeContexts.remove(context);
	}
	
	public Vector<Context> getActiveContexts(){
		return activeContexts;
	}

}
