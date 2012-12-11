package br.ufpe.gprt.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javatuples.Pair;

import br.ufpe.gprt.eventmanager.Subscription;
import br.ufpe.gprt.eventmanager.Subscription.Status;
import br.ufpe.gprt.semantic.ActiveContext;
import br.ufpe.gprt.semantic.Context;
import br.ufpe.gprt.semantic.ContextManager;
import br.ufpe.gprt.semantic.ContextManager.ActionTypeRelatedToCondition;
import br.ufpe.gprt.semantic.ContextManager.Enum_Action;
import br.ufpe.gprt.semantic.PolicyManager;
import br.ufpe.gprt.semantic.PolicyManager.Enum_DataType;
import br.ufpe.gprt.zolertia.RPLMonitor.MetricManager;
import br.ufpe.gprt.zolertia.device.RadioManager;
import br.ufpe.gprt.zolertia.impl.ZolertiaData;
//import br.ufpe.gprt.eventmanager.EventListener;

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
	private static ContextManager contextManager;
	private static PolicyManager policyManager;
	private static ZolertiaData zolertiaData;
	private static RadioManager radioManager;
	private static MetricManager metricManager;

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
			policyManager = new PolicyManager();
			zolertiaData = new ZolertiaData();
			contextManager = new ContextManager();
			radioManager = new RadioManager();
			metricManager = new MetricManager();
		}
		return instance;
	}

	public MetricManager getMetricManager() {
		return metricManager;
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
	
	public RadioManager getRadioManager(){
		return radioManager;
	}

	public synchronized boolean addSubscription(Subscription subscription) {
		boolean result = false;
		for (Context item : contextManager.getPredefinedContexts()) {
			if (item.getTopic().equalsIgnoreCase(subscription.getTopic())) {
				result = contextManager.insertActiveContext(item, subscription);				
				break;
			}
		}
		
		return result;

	}

	public synchronized void removeSubscription(String topic, String endpoint) {
		for (ActiveContext aContext : contextManager.getActiveContexts()) {
			if (topic.equalsIgnoreCase(aContext.getTopic())) {
				aContext.removeSubscriber(endpoint);
			}
		}
	}

	// ----------------------------------------------------------------------------------------------
	public ArrayList<Subscription> getSubscriptionList() {

		ArrayList<Subscription> subscriptionList = new ArrayList<Subscription>();

		for (ActiveContext activeContext : ResourceManager.getInstance()
				.getContextManager().getActiveContexts()) {
			subscriptionList.addAll(activeContext.getInterestedSubscribers());
		}
		return subscriptionList;
	}

	// -----------------------------------------------------------------------------------------------

	public void publishToSubscriber(ActiveContext activeContext,
			List<Pair<Enum_DataType, Double>> dataTypes, boolean inContext) {

		if (activeContext.isSelfManager()) {
			// TODO actions for selfManager based on policies
		} else
			for (Subscription endpoint : activeContext
					.getInterestedSubscribers()) {
				if (endpoint.getStatus() == Status.DEAD) {
					this.removeSubscription(activeContext.getTopic(),
							endpoint.getEndpoint());
					if (activeContext.getInterestedSubscribers().size() == 0)
						contextManager.removeActiveContext(activeContext);
				} else {
					System.out.println(endpoint.getEndpoint()
							+ ": Data is complying with the context - "
							+ activeContext.getTopic());

					String msg = "";
					for (Pair<Enum_DataType, Double> item : dataTypes) {
						msg += item.getValue0().name() + "=" + item.getValue1()
								+ "\n";
						System.out.println("MSG: " + msg);
					}
					endpoint.storeData(msg);

					Map<Enum_Action, ActionTypeRelatedToCondition> activeActions = activeContext
							.getContext().getActions();
					for (Enum_Action item : activeActions.keySet()) {
						if (inContext) {
							if (activeActions.get(item) == ActionTypeRelatedToCondition.IN)
								updateSubscription(endpoint, item);

						} else {
							if (activeActions.get(item) == ActionTypeRelatedToCondition.OUT)
								updateSubscription(endpoint, item);
						}
					}
				}
			}
	}

	private void updateSubscription(Subscription endpoint, Enum_Action item) {
		switch (item) {
		case SEND_PACKETS_LESS_FREQUENTLY:
			endpoint.setPeriod(Subscription.PERIOD_MAX);
			break;
		case SEND_PACKETS_MORE_FREQUENTLY:
			endpoint.setPeriod(Subscription.PERIOD_MIN);
			break;
		case NOTHING:
			boolean defaultMode = false;
			endpoint.setAction(defaultMode);
			break;
		case DEFAULT:
			defaultMode = true;
			endpoint.setAction(defaultMode);
			break;
		default:
			break;
		}
	}


}
