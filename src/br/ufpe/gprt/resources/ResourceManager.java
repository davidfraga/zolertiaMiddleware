package br.ufpe.gprt.resources;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.bind.Marshaller.Listener;

import br.ufpe.gprt.eventmanager.EventCycle;
import br.ufpe.gprt.eventmanager.EventListener;
import br.ufpe.gprt.eventmanager.Part;
import br.ufpe.gprt.eventmanager.Subscription;
import br.ufpe.gprt.semantic.Context;
import br.ufpe.gprt.semantic.ContextManager;
import br.ufpe.gprt.semantic.Policy;
import br.ufpe.gprt.semantic.PolicyManager;
import br.ufpe.gprt.semantic.ContextManager.Enum_Action;
import br.ufpe.gprt.semantic.ContextManager.Enum_Condition;
import br.ufpe.gprt.semantic.ContextManager.Enum_DataType;



public class ResourceManager {

	private static ResourceManager instance;
	private static TreeMap<String, EventListener> listeners;
	private static TreeMap<String, EventCycle> situations;
	private static Vector<ContextManager.Enum_Action> actions;
	private static List<Subscription> subscriptions;
	private static Vector<Context> predefinedContexts;

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
			listeners = new TreeMap<String, EventListener>();
			situations = new TreeMap<String, EventCycle>();
			actions = new Vector<ContextManager.Enum_Action>();
			subscriptions = new ArrayList<Subscription>();
			predefinedContexts = fillContexts();
			
		}
		return instance;
	}
	
	public Vector<Context> getPredefinedContexts(){
		return predefinedContexts;
	}
	private static Vector<Context> fillContexts() {
		Vector<Context> result = new Vector<Context>();
		
		// Human Body Context Construction
		String topic = "human_body_temperature";
		String description = "the human body temperature should not be less than 35ºC or greater than 40ºC";
		int index = ContextManager.selectPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.LESS_THAN, 35, Enum_Action.SEND_PACKETS_MORE_FREQUENTLY);
		Policy h1_policy = PolicyManager.getPolicyByIndex(index);
		index = ContextManager.selectPolicy(Enum_DataType.TEMPERATURE, Enum_Condition.GREATER_THAN, 40, Enum_Action.SEND_PACKETS_MORE_FREQUENTLY);
		Policy h2_policy = PolicyManager.getPolicyByIndex(index);
		
		Vector<Policy> policies = new Vector<Policy>();
		policies.add(h1_policy);
		policies.add(h2_policy);
		Context humanbody = new Context(topic, description, policies);
		
		result.add(humanbody);
		// End of Human Body Context Construction
		
		
		return result;
	}

	public synchronized long getReceivedEvents() {
		return listeners.size();
	}

	
	public synchronized void activateEventPattern(EventListener listener) {
		// TODO Auto-generated method stub

	}

	
	public synchronized void deactivateEventListener(EventListener listener) {
		// TODO Auto-generated method stub

	}

	
	public synchronized void saveToFile() {
		// TODO Auto-generated method stub

	}

	
	public synchronized void loadFromFile() {
		// TODO Auto-generated method stub

	}

	
	public synchronized void activateSituationListener(EventCycle situation) {
		// TODO Auto-generated method stub

	}

	
	public synchronized void deactivateSituationListener(String situationName) {
		// TODO Auto-generated method stub

	}

	
	public synchronized void activatePolicy(Policy policy) {
		// TODO Auto-generated method stub

	}

	
	public synchronized void deactivatePolicy(Policy policy) {
		// TODO Auto-generated method stub

	}

	
	public synchronized void loadRulesFromFile() throws Exception {
		// TODO Auto-generated method stub

	}

	
	public synchronized void addEventListener(String name,
			EventListener eventListener) {
		this.listeners.put(name, eventListener);
	}

	
	public synchronized void removeEventListener(String eventListenerName) {
		listeners.remove(eventListenerName);
	}

	
	public synchronized Vector<ContextManager.Enum_Action> getAvailableActions() {
		return actions;
	}

	public synchronized TreeMap<String, EventListener> getRegisteredListeners() {
		return listeners;
	}

	
	public synchronized TreeMap<String, EventCycle> getSituations() {
		return situations;
	}

	public synchronized void addSubscription(Subscription subscription) {
		subscriptions.add(subscription);
	}

	public synchronized void removeSubscription(String topic, String endpoint) {
		for (Subscription item : subscriptions) {
			if (item.getEndpoint().equals(endpoint) && item.getTopic().equals(topic)) subscriptions.remove(item);
		}
	}

	public synchronized Subscription[] getAllSubscriptions() {
		return (Subscription[]) subscriptions.toArray();
	}

	public synchronized void policyCheck(
			br.ufpe.gprt.semantic.Policy individualPolicy,
			SensorValues currentSensorValue) {

		boolean policyEvaluation = true;
		int atualData = 0;

		if (individualPolicy.getDataType() == 0) {
			atualData = currentSensorValue.getTemperature();
		}
		if (individualPolicy.getDataType() == 1) {
			atualData = currentSensorValue.getBatteryInformation();
		}
		if (individualPolicy.getDataType() == 2) {
			atualData = currentSensorValue.getRssi();
		}

		if (individualPolicy.getDataType() == 3) {
			atualData = currentSensorValue.getAtualChannel();
		}

		if (individualPolicy.getDataType() == 4) {
			atualData = currentSensorValue.getHopCount();
		}

		if (individualPolicy.getConditionCode() == 0)
			policyEvaluation = (atualData > individualPolicy
					.getConditionParam());

		if (individualPolicy.getConditionCode() == 1)
			policyEvaluation = (atualData < individualPolicy
					.getConditionParam());

		if (individualPolicy.getConditionCode() == 2)
			policyEvaluation = (atualData == individualPolicy
					.getConditionParam());

		System.out.println("Action taken:");

		if (policyEvaluation) {

			if (individualPolicy.getActionCode() == 0) {
				System.out.println("Enviar dados com menos frequencia");
			}
			if (individualPolicy.getActionCode() == 1) {
				System.out.println("Enviar dados com mais frequencia");
			}
			if (individualPolicy.getActionCode() == 2) {
				System.out.println("Rebootar");
			}

		} else {
			System.out.println("Nothing");
		}

	}

	public synchronized boolean clearSubscriptionsEndpoint(String endpoint) {
		boolean result = false;
		for (Subscription item : subscriptions) {
			if (item.getEndpoint().equals(endpoint)) {
				subscriptions.remove(item);
				result = true;
			}
		}
		return result;
	}

	public synchronized boolean publish(String topic, Part[] in1) throws RemoteException {
		boolean result = false;
		for (Subscription item : subscriptions) {
			if (item.getTopic().equals(topic)) {
				item.setParts(in1);
				item.sendData();
				result = true;
			}
		}
		return result;
	}

	public String extractReport(Part[] parts) {
		String data = "BEMO-COFRA REPORT\n";
		for (Part item : parts) {
			data += item.getKey() + "=" + item.getValue() + "\n";
		}
		return data;
	}
}
