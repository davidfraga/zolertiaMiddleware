package br.ufpe.gprt.eventmanager.impl;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import br.ufpe.gprt.eventmanager.EventManagerPort;
import br.ufpe.gprt.eventmanager.Subscription;
import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.semantic.Context;
import eu.linksmart.clients.RemoteWSClientProvider;
import eu.linksmart.network.NetworkManagerApplication;

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

	}

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

	@Override
	public boolean subscribe(String topic, String endpoint)
			throws RemoteException {
		boolean topicExists = false;
		
		// Ensure if this topic is already predefinied
		for (Context item : ResourceManager.getInstance().getContextManager().getPredefinedContexts()) {
			if (item.getTopic().equals(topic)) {
				topicExists = true;
				break;
			}
		}		
		
		if (topicExists) {
			Subscription subscriber = new Subscription();
			subscriber.setTopic(topic);
			subscriber.setEndpoint(endpoint);
			ResourceManager.getInstance().addSubscription(subscriber);
		}
		return topicExists;
	}

	@Override
	public boolean unsubscribe(String topic, String endpoint)
			throws RemoteException {
		ResourceManager.getInstance().removeSubscription(topic, endpoint);
		return true;
	}

	@Override
	public boolean subscribeWithHID(String topic, String hid)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unsubscribeWithHID(String topic, String hid)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String poll(String topic) throws RemoteException {
		String result = "";
		for (Subscription item : ResourceManager.getInstance()
				.getAllSubscriptions()) {
			if (item.getTopic().equals(topic))
				result += item.extractReport()+ "\n";
		}
		return result;
	}

}
