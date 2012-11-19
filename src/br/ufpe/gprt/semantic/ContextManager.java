package br.ufpe.gprt.semantic;
import java.util.ArrayList;
import java.util.Vector;

import br.ufpe.gprt.eventmanager.Subscription;
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

	private ArrayList<Context> predefinedContexts;
	private ArrayList<ActiveContext> activeContexts;

	public ContextManager() {
		predefinedContexts = new ArrayList<Context>();
		activeContexts = new ArrayList<ActiveContext>();
		fillContexts();
	}
	
	public ArrayList<Context> getPredefinedContexts(){
		return predefinedContexts;
	}
	
	/**
	 * Create predefined contexts
	 */
	public void fillContexts() {
/*		
		// Human Body Context Construction
		String topic = "human_body_temperature";
		String description = "the human body temperature must be greater than 35ºC or less than 40ºC";
		
		Policy h1_policy = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.GREATER_THAN,35);
		Policy h2_policy = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.LESS_THAN, 40);
		Enum_Action actionIn = Enum_Action.SEND_PACKETS_LESS_FREQUENTLY;
		Enum_Action actionOut = Enum_Action.SEND_PACKETS_MORE_FREQUENTLY;
		ArrayList<Policy> policies = new ArrayList<Policy>();
		policies.add(h1_policy);
		policies.add(h2_policy);
		Context humanbody = new Context(topic, description, policies, actionIn, actionOut);
		
		predefinedContexts.add(humanbody);
		// End of Human Body Context Construction
		*/
		
		// TESTE
		String topic = "human_body_temperature";
		String description = "the human body temperature must be greater than 35ºC or less than 40ºC";
		
		Policy h1_policy = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.GREATER_THAN,25);
		//Policy h2_policy = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.LESS_THAN, 40);
		Enum_Action actionIn = Enum_Action.SEND_PACKETS_LESS_FREQUENTLY;
		Enum_Action actionOut = Enum_Action.SEND_PACKETS_MORE_FREQUENTLY;
		ArrayList<Policy> policies = new ArrayList<Policy>();
		policies.add(h1_policy);
		//policies.add(h2_policy);
		Context humanbody = new Context(topic, description, policies, actionIn, actionOut);
		
		predefinedContexts.add(humanbody);
		
		
		String topic2 = "human_body_temperature2";
		String description2 = "the human body temperature must be greater than 35ºC or less than 40ºC";
		
		Policy h1_policy2 = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.GREATER_THAN,30);
		//Policy h2_policy = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.LESS_THAN, 40);
		Enum_Action actionIn2 = Enum_Action.SEND_PACKETS_LESS_FREQUENTLY;
		Enum_Action actionOut2 = Enum_Action.SEND_PACKETS_MORE_FREQUENTLY;
		ArrayList<Policy> policies2 = new ArrayList<Policy>();
		policies2.add(h1_policy2);
		//policies.add(h2_policy);
		Context humanbody2 = new Context(topic2, description2, policies2, actionIn2, actionOut2);
		
		predefinedContexts.add(humanbody2);
		// Fim do teste
		
		
		//Cold Context Construction
		String topic1 = "Cold";
		String description1 = "the Temperature should not be less than 20 ";
		ArrayList<Policy> policies1 = new ArrayList<Policy>();
		Policy h1_policy1 = ResourceManager.getInstance().getPolicyManager().createPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.LESS_THAN,20);
		Enum_Action actionOut1 = Enum_Action.REBOOT;
		Enum_Action actionIn1 = Enum_Action.SEND_PACKETS_MORE_FREQUENTLY;
		policies1.add(h1_policy1);
		Context cold = new Context(topic1, description1, policies, actionIn1, actionOut1);
		predefinedContexts.add(cold);
	
	}
	
	public void activateContextAction(ActiveContext context, boolean insideRule) {
		System.out.println("Action Taken:");
		if (insideRule){
			if (context.getContext().getActionIn() == Enum_Action.SEND_PACKETS_LESS_FREQUENTLY) {
				System.out.println("Send packets less frequently");
			}
			if (context.getContext().getActionIn() == Enum_Action.SEND_PACKETS_MORE_FREQUENTLY) {
				System.out.println("Send packets more frequently");
			}
			if (context.getContext().getActionIn() == Enum_Action.REBOOT) {
				System.out.println("Reboot");
			}
		}
		
		else {
			if (context.getContext().getActionOut() == Enum_Action.SEND_PACKETS_LESS_FREQUENTLY) {
				System.out.println("Send packets less frequently");
			}
			if (context.getContext().getActionOut() == Enum_Action.SEND_PACKETS_MORE_FREQUENTLY) {
				System.out.println("Send packets more frequently");
			}
			if (context.getContext().getActionOut() == Enum_Action.REBOOT) {
				System.out.println("Reboot");
			}
		}
	}
	
	public void insertActiveContext(Context context, String endpoint){
			ActiveContext newActiveContext = new ActiveContext(context);
			newActiveContext.addInterestedSubscriber(endpoint);
			activeContexts.add(newActiveContext);	
	}
	
	public void removeActiveContext(Context context){
		this.activeContexts.remove(context);
	}
	
	public ArrayList<ActiveContext> getActiveContexts(){
		return activeContexts;
	}

}
