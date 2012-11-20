package br.ufpe.gprt.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

//import br.ufpe.gprt.eventmanager.EventListener;
import br.ufpe.gprt.eventmanager.Subscription;
import br.ufpe.gprt.semantic.ActiveContext;
import br.ufpe.gprt.semantic.ContextManager;
import br.ufpe.gprt.semantic.ContextManager.Enum_Action;
import br.ufpe.gprt.semantic.PolicyManager;
import br.ufpe.gprt.semantic.PolicyManager.Enum_DataType;
import br.ufpe.gprt.zolertia.impl.ZolertiaData;

/**
 * Manages all resources from zolertia middleware. It's a singleton approach
 * There are others managers supported: ContextManager, PolicyManagar. The
 * exchange of zolertia information is done through the ZolertiaData instance
 * 
 * @author GPRT-BEMO
 * 
 */
public class ResourceManager {

	private static ResourceManager instance;
	//private static TreeMap<String, EventListener> listeners;
	private static Vector<Enum_Action> actions;
	private static Map<String, Subscription> subscriptions;
	private static ContextManager contextManager;
	private static PolicyManager policyManager;
	private static ZolertiaData zolertiaData;

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
			//listeners = new TreeMap<String, EventListener>();
			actions = new Vector<Enum_Action>();
			subscriptions = new HashMap<String, Subscription>();
			policyManager = new PolicyManager();
			zolertiaData = new ZolertiaData();
			contextManager = new ContextManager();
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
/*
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

	public synchronized Vector<Enum_Action> getAvailableActions() {
		return actions;
	}

	public synchronized TreeMap<String, EventListener> getRegisteredListeners() {
		return listeners;
	}*/

	public synchronized void addSubscription(Subscription subscription) {
		subscriptions.put(subscription.getEndpoint(), subscription);
	}

	public synchronized void removeSubscription(String topic, String endpoint) {

		subscriptions.remove(endpoint);

	}
	
	
	//----------------------------------------------------------------------------------------------
	public ArrayList<Subscription> getSubscriptionList(){
		
		ArrayList<Subscription> subscriptionList = new ArrayList<Subscription>();
		Subscription currentSubscription = new Subscription();
		
		for( ActiveContext activeContext: ResourceManager.getInstance().getContextManager().getActiveContexts()){
			
			for(String endpoint : activeContext.getInterestedSubscribers()){
				currentSubscription.setTopic(activeContext.getTopic());
				currentSubscription.setEndpoint(endpoint);
				subscriptionList.add(currentSubscription);
			}
		}
		return subscriptionList;
	}
	//-----------------------------------------------------------------------------------------------
	
	public synchronized Collection<Subscription> getAllSubscriptions() {
		return subscriptions.values();
	}

	public void publishToSubscriber(ActiveContext activeContext,
			boolean dataEvaluated, Map<Enum_DataType, Double> dataTypes) {
		for (String endpoints : activeContext.getInterestedSubscribers()) {

			if (dataEvaluated) {
				System.out.println(endpoints
						+ ": Data is complying with the context - "
						+ activeContext.getTopic());
				Subscription sub = subscriptions.get(endpoints);
				System.out.println(sub.getTopic());
				String msg = "";
				for (Enum_DataType type : dataTypes.keySet()) {
					msg += type.name() + "=" + dataTypes.get(type) + "\n";
					System.out.println("MSG: " + msg);
				}
				sub.sendData(msg);
			}

			else {
				System.out.println(endpoints
						+ ": Data isn't complying with the context - "
						+ activeContext.getTopic());
			}

		}
	}

}
