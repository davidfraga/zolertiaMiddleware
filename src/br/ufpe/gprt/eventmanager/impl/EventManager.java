package br.ufpe.gprt.eventmanager.impl;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import br.ufpe.gprt.eventmanager.EventManagerPort;
import br.ufpe.gprt.eventmanager.Subscription;
import br.ufpe.gprt.eventmanager.Subscription.Sender;
import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.semantic.ActiveContext;
import br.ufpe.gprt.semantic.Context;
import eu.linksmart.clients.RemoteWSClientProvider;
import eu.linksmart.network.NetworkManagerApplication;

/**
 * Class that process webservice data incoming from client side
 * 
 * @author GPRT-BEMO
 * 
 */
public class EventManager implements EventManagerPort {

	private static final String SID = "zolertiaMiddleware";
	private static final String OSGI_SERVICE_HTTP_PORT = System
			.getProperty("org.osgi.service.http.port");
	private static final String AXIS_SERVICES_PATH = "http://localhost:"
			+ OSGI_SERVICE_HTTP_PORT + "/axis/services/";
	private static final String ENDPOINT = AXIS_SERVICES_PATH
			+ EventManagerPort.class.getSimpleName();

	private Logger LOG = Logger.getLogger(EventManager.class.getName());
	private BundleContext bundleContext;
	private String myHID;
	private NetworkManagerApplication networkManager;

	/**
	 * Starting service into linksmart network manager
	 * 
	 * @param ctx
	 */
	protected void activate(ComponentContext ctx) {

		LOG.debug("Activating "
				+ ctx.getBundleContext().getBundle().getSymbolicName());

		this.bundleContext = ctx.getBundleContext();

		// get NetworkManager
		RemoteWSClientProvider service = (RemoteWSClientProvider) this.bundleContext
				.getService(this.bundleContext
						.getServiceReference(RemoteWSClientProvider.class
								.getName()));

		try {
			networkManager = (NetworkManagerApplication) service
					.getRemoteWSClient(
							NetworkManagerApplication.class.getName(),
							AXIS_SERVICES_PATH + "NetworkManagerApplication",
							false);
		} catch (Exception e1) {
			LOG.error(e1.getMessage(), e1.getCause());
		}
		try {
			myHID = networkManager.createHIDwDesc(SID, ENDPOINT);

		} catch (RemoteException e) {
			// LOG.error(e.getMessage());
			e.printStackTrace();
		}
		LOG.debug("Started "
				+ ctx.getBundleContext().getBundle().getSymbolicName());

		ResourceManager.getInstance();

	}

	/**
	 * Stopping service and local network manager
	 * 
	 * @param ctx
	 */
	protected void deactivate(ComponentContext ctx) {

		LOG.debug("Stopping "
				+ ctx.getBundleContext().getBundle().getSymbolicName());

		try {
			networkManager.removeHID(myHID);
		} catch (RemoteException e) {
			LOG.error(e.getMessage(), e.getCause());
		}
		networkManager = null;

		LOG.debug("Stopped "
				+ ctx.getBundleContext().getBundle().getSymbolicName());
	}

	/**
	 * Subscribe a client If the context is already predefined, make a
	 * subscription
	 * 
	 * @param topic
	 *            - context, ex.: human_body_temperature
	 * @param endpoint
	 *            - at the moment, just the IP:Port for where we send
	 *            notifications
	 */
	@Override
	public boolean subscribe(String topic, String endpoint)
			throws RemoteException {

		Subscription subscriber = new Subscription(topic, endpoint);
		Thread subscription = new Thread(subscriber);
		ResourceManager.getInstance().addSubscription(subscriber);
		subscription.start();

		return true;
	}

	/**
	 * Unsubscribe the client
	 * 
	 * @param topic
	 *            - context, ex.: human_body_temperature
	 * @param endpoint
	 *            - at the moment, just the IP:Port for where we send
	 *            notifications
	 */
	@Override
	public boolean unsubscribe(String topic, String endpoint)
			throws RemoteException {

		// ResourceManager.getInstance().removeSubscription(topic, endpoint);
		System.out.println("MANAGER: unsubscribing " + endpoint);
		// Remover o susbscriber na lista de contextos ativos
		
		ResourceManager.getInstance().removeSubscription(topic,
				endpoint);
		for (ActiveContext activeContext : ResourceManager.getInstance()
				.getContextManager().getActiveContexts()) {
			if (activeContext.getTopic().equalsIgnoreCase(topic)) {
				activeContext.removeSubscriber(endpoint);
//				if (activeContext.getInterestedSubscribers().size() == 0)
//					ResourceManager.getInstance().getContextManager()
//							.removeActiveContext(activeContext);

				System.out.println("MANAGER: " + endpoint + " removed!");
				break;
			}
		}

		// Remover o subscriber na lista de subscribers do resource manager

		return true;
	}

	/**
	 * Get a synchronous report
	 * 
	 * @param topic
	 *            - context, ex.: human_body_temperature
	 */
	@Override
	public String poll(String topic) throws RemoteException {
		String result = "";
		// for (Subscription item : ResourceManager.getInstance()
		// .getAllSubscriptions()) {
		// if (item.getTopic().equals(topic))
		// result += item.extractReport() + "\n";
		// }
		return result;
	}

}
