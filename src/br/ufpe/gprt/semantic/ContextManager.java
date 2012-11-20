package br.ufpe.gprt.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.semantic.PolicyManager.Enum_Condition;
import br.ufpe.gprt.semantic.PolicyManager.Enum_DataType;
import br.ufpe.gprt.zolertia.deviceCommandProxy.CommandFormat;
import br.ufpe.gprt.zolertia.deviceCommandProxy.CommandFormat.CommandType;

/**
 * Manages the context available. It has predefined contexts and active contexts
 * 
 * @author GPRT-BEMO
 * 
 */
public class ContextManager {

	private List<Context> predefinedContexts;
	private List<ActiveContext> activeContexts;

	public ContextManager() {
		predefinedContexts = new ArrayList<Context>();
		activeContexts = new ArrayList<ActiveContext>();
		fillContexts();
	}

	public List<Context> getPredefinedContexts() {
		return predefinedContexts;
	}

	/**
	 * Create predefined contexts
	 */
	public void fillContexts() {

		// Pre-defined context: "human_body_temperature" T>25 - send packets
		// more frequently
		String topic = "human_body_temperature";
		String description = "the human body temperature must be greater than 25ºC and less than 30ºC";

		Policy h1_policy = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.TEMPERATURE,
						Enum_Condition.GREATER_THAN, 25);
		Policy h2_policy = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.TEMPERATURE,
						Enum_Condition.LESS_THAN, 30);

		Enum_Action actionIn = Enum_Action.SEND_PACKETS_LESS_FREQUENTLY;
		Enum_Action actionOut = Enum_Action.SEND_PACKETS_MORE_FREQUENTLY;
		ArrayList<Policy> policies = new ArrayList<Policy>();
		policies.add(h1_policy);

		Map<Enum_Action, ActionTypeRelatedToCondition> actions = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions.put(actionIn, ActionTypeRelatedToCondition.IN);
		actions.put(actionOut, ActionTypeRelatedToCondition.OUT);
		Context humanbody = new Context(topic, description, policies, actions);

		predefinedContexts.add(humanbody);

		// Pre-defined context: human body temperature 2 T
		String topic2 = "human_body_temperature2";
		String description2 = "the human body temperature must be less than 30ºC";

		Policy h1_policy2 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.TEMPERATURE,
						Enum_Condition.LESS_THAN, 30);
		Enum_Action actionOut2 = Enum_Action.SEND_PACKETS_LESS_FREQUENTLY;
		Enum_Action actionIn2 = Enum_Action.SEND_PACKETS_MORE_FREQUENTLY;
		ArrayList<Policy> policies2 = new ArrayList<Policy>();
		policies2.add(h1_policy2);

		Map<Enum_Action, ActionTypeRelatedToCondition> actions2 = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions2.put(actionIn2, ActionTypeRelatedToCondition.IN);
		actions2.put(actionOut2, ActionTypeRelatedToCondition.OUT);

		Context humanbody2 = new Context(topic2, description2, policies2,
				actions2);

		predefinedContexts.add(humanbody2);

		// Pre-defined context: Cold Context Construction
		String topic1 = "Cold";
		String description1 = "the Temperature should not be less than 20 ";
		ArrayList<Policy> policies1 = new ArrayList<Policy>();
		Policy h1_policy1 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.TEMPERATURE,
						Enum_Condition.LESS_THAN, 20);
		Enum_Action actionOut1 = Enum_Action.REBOOT;
		Enum_Action actionIn1 = Enum_Action.SEND_PACKETS_MORE_FREQUENTLY;
		policies1.add(h1_policy1);

		Map<Enum_Action, ActionTypeRelatedToCondition> actions1 = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions1.put(actionIn1, ActionTypeRelatedToCondition.IN);
		actions1.put(actionOut1, ActionTypeRelatedToCondition.OUT);

		Context cold = new Context(topic1, description1, policies, actions1);
		predefinedContexts.add(cold);

		// Pre-defined context: Context High_temperature - ALERT example
		String topic3 = "high_temperature";
		String description3 = "Temperature never can be greather than 35";
		ArrayList<Policy> policies3 = new ArrayList<Policy>();
		Policy policy3 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.TEMPERATURE,
						Enum_Condition.GREATER_THAN, 35);
		Enum_Action actionEnter3 = Enum_Action.SEND_PACKET_IMMEDIATELY;
		Enum_Action actionLeave3 = Enum_Action.NOTHING;
		policies3.add(policy3);

		Map<Enum_Action, ActionTypeRelatedToCondition> actions3 = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions3.put(actionEnter3,
				ActionTypeRelatedToCondition.ENTER);
		actions3.put(actionLeave3,
				ActionTypeRelatedToCondition.LEAVE);

		Context high_temperature = new Context(topic3, description3, policies3,
				actions3);
		predefinedContexts.add(high_temperature);

		// Pre-defined context: Context Low_temperature
		String topic4 = "low_temperature";
		String description4 = "Temperature should not be less than 20";
		ArrayList<Policy> policies4 = new ArrayList<Policy>();
		Policy policy4 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.TEMPERATURE,
						Enum_Condition.LESS_THAN, 20);
		Enum_Action actionOut4 = Enum_Action.NOTHING;
		Enum_Action actionIn4 = Enum_Action.NOTHING;
		policies4.add(policy4);

		Map<Enum_Action, ActionTypeRelatedToCondition> actions4 = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions4.put(actionIn4, ActionTypeRelatedToCondition.IN);
		actions4.put(actionOut4, ActionTypeRelatedToCondition.OUT);

		Context low_temperature = new Context(topic4, description4, policies4,
				actions4);
		predefinedContexts.add(low_temperature);

		// Pre-defined context: Context Crazy temperature
		String topic5 = "crazy_temperature";
		String description5 = "Temperature should not be greather than 50 and less than 15";
		ArrayList<Policy> policies5 = new ArrayList<Policy>();
		Policy policy51 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.TEMPERATURE,
						Enum_Condition.GREATER_THAN, 50);
		Policy policy52 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.TEMPERATURE,
						Enum_Condition.LESS_THAN, 15);
		Enum_Action actionOut5 = Enum_Action.NOTHING;
		Enum_Action actionIn5 = Enum_Action.NOTHING;
		policies5.add(policy51);
		policies5.add(policy52);

		Map<Enum_Action, ActionTypeRelatedToCondition> actions5 = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions5.put(actionIn5, ActionTypeRelatedToCondition.IN);
		actions5.put(actionOut5, ActionTypeRelatedToCondition.OUT);

		Context crazy_temperature = new Context(topic5, description5,
				policies5, actions5);
		predefinedContexts.add(crazy_temperature);

		// Pre-defined context: Context Less_interference
		String topic6 = "less_interference";
		String description6 = "The system generate the less possible interference";
		ArrayList<Policy> policies6 = new ArrayList<Policy>();
		Policy policy6 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.RSSI, Enum_Condition.GREATER_THAN,
						40);
		Enum_Action actionOut6 = Enum_Action.NOTHING;
		Enum_Action actionIn6 = Enum_Action.SEND_PACKETS_LESS_FREQUENTLY;
		policies6.add(policy6);
		Map<Enum_Action, ActionTypeRelatedToCondition> actions6 = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions6.put(actionIn6, ActionTypeRelatedToCondition.IN);
		actions6.put(actionOut6, ActionTypeRelatedToCondition.OUT);
		Context less_interference = new Context(topic6, description6,
				policies6, actions6);
		predefinedContexts.add(less_interference);

		// Pre-defined context: Context Battery save
		String topic7 = "battery_save";
		String description7 = "If the battery information is less than 20 %, the mote must pass to send fewer messages";
		ArrayList<Policy> policies7 = new ArrayList<Policy>();
		Policy policy7 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.BATERY_INFORMATION,
						Enum_Condition.LESS_THAN, 20);
		Enum_Action actionOut7 = Enum_Action.NOTHING;
		Enum_Action actionIn7 = Enum_Action.SEND_PACKETS_LESS_FREQUENTLY;
		policies7.add(policy7);

		Map<Enum_Action, ActionTypeRelatedToCondition> actions7 = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions7.put(actionIn7, ActionTypeRelatedToCondition.IN);
		actions7.put(actionOut7, ActionTypeRelatedToCondition.OUT);

		Context battery_save = new Context(topic7, description7, policies7,
				actions7);
		predefinedContexts.add(battery_save);

		// Pre-defined context: Context Less network size
		String topic8 = "Less_network_size";
		String description8 = "limits the extent of network topology";
		ArrayList<Policy> policies8 = new ArrayList<Policy>();
		Policy policy8 = ResourceManager
				.getInstance()
				.getPolicyManager()
				.createPolicy(Enum_DataType.HOP_COUNT,
						Enum_Condition.LESS_THAN, 2);
		Enum_Action actionOut8 = Enum_Action.NOTHING;
		Enum_Action actionIn8 = Enum_Action.CHANGE_METRIC_0FUNCTION;
		policies8.add(policy8);

		Map<Enum_Action, ActionTypeRelatedToCondition> actions8 = new HashMap<ContextManager.Enum_Action, ContextManager.ActionTypeRelatedToCondition>();
		actions8.put(actionIn8, ActionTypeRelatedToCondition.IN);
		actions8.put(actionOut8, ActionTypeRelatedToCondition.OUT);

		Context less_network_size = new Context(topic8, description8,
				policies8, actions8);
		predefinedContexts.add(less_network_size);

	}

	public void activateContextAction(ActiveContext activeContext) {
		System.out.println("Action Taken:");
		Context context = activeContext.getContext();
		for (Enum_Action item : context.getActions().keySet()) {
			if (item == Enum_Action.SEND_PACKETS_LESS_FREQUENTLY
					|| item == Enum_Action.SEND_PACKETS_MORE_FREQUENTLY)
				ResourceManager
						.getInstance()
						.getZolertiaData()
						.sendZolertiaCommand(
								CommandFormat.getTemperatureCommand(
										CommandType.INSERT, context
												.getPolicies(), item, context
												.getActions().get(item)));
			if (item == Enum_Action.REBOOT)
				ResourceManager.getInstance().getZolertiaData()
						.sendZolertiaCommand(item.name().toLowerCase());

		}
	}
	
	public void deactivateContextAction(ActiveContext activeContext) {		
		Context context = activeContext.getContext();
		System.out.println("Deactivating context "+context.getTopic());
		for (Enum_Action item : context.getActions().keySet()) {
			if (item == Enum_Action.SEND_PACKETS_LESS_FREQUENTLY
					|| item == Enum_Action.SEND_PACKETS_MORE_FREQUENTLY)
				ResourceManager
						.getInstance()
						.getZolertiaData()
						.sendZolertiaCommand(
								CommandFormat.getTemperatureCommand(
										CommandType.DELETE, context
												.getPolicies(), item, context
												.getActions().get(item)));
			if (item == Enum_Action.REBOOT)
				ResourceManager.getInstance().getZolertiaData()
						.sendZolertiaCommand(item.name().toLowerCase());

		}
	}

	public void insertActiveContext(Context context, String endpoint) {
		if (predefinedContexts.contains(context)) {
			boolean activeContextFound = false;

			for (ActiveContext item : activeContexts) {
				if (item.getContext() == context) {
					item.addInterestedSubscriber(endpoint);
					activeContextFound = true;
					break;
				}
			}

			if (!activeContextFound) {
				ActiveContext newActiveContext = new ActiveContext(context);
				newActiveContext.addInterestedSubscriber(endpoint);
				activeContexts.add(newActiveContext);
				activateContextAction(newActiveContext);
			}

		}
	}

	public void removeActiveContext(ActiveContext context) {		
		this.deactivateContextAction(context);
		this.activeContexts.remove(context);
	}

	public List<ActiveContext> getActiveContexts() {
		return activeContexts;
	}

	public enum Enum_Action {
		SEND_PACKETS_LESS_FREQUENTLY, SEND_PACKETS_MORE_FREQUENTLY, REBOOT, STOP_POLICY, CHANGE_METRIC_BATTERY_LEVEL, CHANGE_METRIC_0FUNCTION, CHANGE_METRIC_ETX, NOTHING, SEND_PACKET_IMMEDIATELY
	}

	public enum ActionTypeRelatedToCondition {
		IN, OUT, ENTER, LEAVE
	}

}
