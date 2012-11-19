package br.ufpe.gprt.eventmanager.impl;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import br.ufpe.gprt.eventmanager.EventManagerPort;
import br.ufpe.gprt.eventmanager.Subscription;
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
		boolean topicExists = false;
		boolean topicActive = false;
		// Ensure if this topic is already predefinied
		for (Context item : ResourceManager.getInstance().getContextManager()
				.getPredefinedContexts()) {
			System.out.println("Contexto pre definido : " + item.getTopic());
			if (item.getTopic().equals(topic)) {
				System.out
						.println("Contexto pre definido = ao topico desejado ");
				for (ActiveContext activeContext : ResourceManager
						.getInstance().getContextManager().getActiveContexts()) {
					System.out.println("Contexto ativo :"
							+ activeContext.getTopic());
					if (activeContext.getTopic().equals(topic)) {
						System.out
								.println("O contexto ativo ja existe...adicionando subscriber interessado :"
										+ endpoint);
						activeContext.addInterestedSubscriber(endpoint);
						topicActive = true;
						break;
					}

				}

				if (!topicActive) {
					System.out.println("Adicionou novo contexto ativo :"
							+ item.getTopic() + "e adicionou o subscriber "
							+ endpoint);
					ResourceManager.getInstance().getContextManager()
							.insertActiveContext(item, endpoint);
				}

				Subscription subscriber = new Subscription();
				subscriber.setTopic(topic);
				subscriber.setEndpoint(endpoint);
				ResourceManager.getInstance().addSubscription(subscriber);

				return true;
			}
		}

		return false;
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
		
		//ResourceManager.getInstance().removeSubscription(topic, endpoint);
		System.out.println("MANAGER: unsubscribing "+endpoint);
		//Remover o susbscriber na lista de contextos ativos
		for ( ActiveContext activeContext : ResourceManager.getInstance().getContextManager().getActiveContexts()){
			if(activeContext.getTopic().equalsIgnoreCase(topic)){
				activeContext.removeSubscriber(endpoint);
				ResourceManager.getInstance().removeSubscription(topic, endpoint);
				System.out.println("MANAGER: "+endpoint+" removed!");
				break;
			}
		}
		
		//Remover o subscriber na lista de subscribers do resource manager
		
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
//		for (Subscription item : ResourceManager.getInstance()
//				.getAllSubscriptions()) {
//			if (item.getTopic().equals(topic))
//				result += item.extractReport() + "\n";
//		}
		return result;
	}

}
