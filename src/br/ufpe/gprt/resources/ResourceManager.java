package br.ufpe.gprt.resources;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import br.ufpe.gprt.eventmanager.EventListener;
import br.ufpe.gprt.eventmanager.Part;
import br.ufpe.gprt.eventmanager.Subscription;
import br.ufpe.gprt.semantic.Context;
import br.ufpe.gprt.semantic.ContextManager;
import br.ufpe.gprt.semantic.Policy;
import br.ufpe.gprt.semantic.PolicyManager;
import br.ufpe.gprt.semantic.PolicyManager.Enum_Action;
import br.ufpe.gprt.semantic.PolicyManager.Enum_Condition;
import br.ufpe.gprt.semantic.PolicyManager.Enum_DataType;
import br.ufpe.gprt.zolertia.device.SensorData;
import br.ufpe.gprt.zolertia.impl.ZolertiaData;

/**
 * Manages all resources from zolertia middleware. It's a singleton approach
 * There are others managers supported: ContextManager, PolicyManagar. 
 * The exchange of zolertia information is done through the ZolertiaData instance 
 * @author GPRT-BEMO
 *
 */
public class ResourceManager {

	private static ResourceManager instance;
	private static TreeMap<String, EventListener> listeners;
	private static Vector<PolicyManager.Enum_Action> actions;
	private static List<Subscription> subscriptions;
	private static ContextManager contextManager;
	private static PolicyManager policyManager;
	private static ZolertiaData zolertiaData;

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
			listeners = new TreeMap<String, EventListener>();
			actions = new Vector<PolicyManager.Enum_Action>();
			subscriptions = new ArrayList<Subscription>();
			contextManager = new ContextManager();
			policyManager = new PolicyManager();
			zolertiaData = new ZolertiaData();
		}
		return instance;
	}

	public ContextManager getContextManager() {
		return contextManager;
	}

	public PolicyManager getPolicyManager() {
		return policyManager;
	}
	
	public ZolertiaData getZolertiaData() {
		return zolertiaData;
	}

	public synchronized long getReceivedEvents() {
		return listeners.size();
	}

	public synchronized void addEventListener(String name,
			EventListener eventListener) {
		this.listeners.put(name, eventListener);
	}

	public synchronized void removeEventListener(String eventListenerName) {
		listeners.remove(eventListenerName);
	}

	public synchronized Vector<PolicyManager.Enum_Action> getAvailableActions() {
		return actions;
	}

	public synchronized TreeMap<String, EventListener> getRegisteredListeners() {
		return listeners;
	}

	public synchronized void addSubscription(Subscription subscription) {
		subscriptions.add(subscription);
	}

	public synchronized void removeSubscription(String topic, String endpoint) {
		for (Subscription item : subscriptions) {
			if (item.getEndpoint().equals(endpoint)
					&& item.getTopic().equals(topic))
				subscriptions.remove(item);
		}
	}

	public synchronized Subscription[] getAllSubscriptions() {
		return (Subscription[]) subscriptions.toArray();
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

	/**
	 * Public the information (in1) to relative subscribers
	 * @param topic
	 * @param in1
	 * @return
	 * @throws RemoteException
	 */
	public synchronized boolean publish(String topic, Part[] in1)
			throws RemoteException {
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

}
