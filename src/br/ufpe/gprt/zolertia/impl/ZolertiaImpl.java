package br.ufpe.gprt.zolertia.impl;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import br.ufpe.gprt.zolertia.IZolertia;
import br.ufpe.gprt.zolertia.comm.SerialConnection;
import br.ufpe.gprt.zolertia.comm.SerialDumpConnection;

import eu.linksmart.clients.RemoteWSClientProvider;
import eu.linksmart.network.NetworkManagerApplication;

/**
 * Encapsulates communication with an Arduino sensor platform. Provides an
 * interface to request the latest sensor values, set the LED status, and
 * register as an event listener to receive updates about sensor values.
 * 
 * @author simon
 */
public class ZolertiaImpl implements IZolertia {

	private static final String SID = IZolertia.class.getSimpleName();
	private static final String OSGI_SERVICE_HTTP_PORT = System
			.getProperty("org.osgi.service.http.port");
	private static final String AXIS_SERVICES_PATH = "http://localhost:"
			+ OSGI_SERVICE_HTTP_PORT + "/axis/services/";
	private static final String ENDPOINT = AXIS_SERVICES_PATH
			+ IZolertia.class.getSimpleName();

	private Logger LOG = Logger.getLogger(ZolertiaImpl.class.getName());

	// private SerialCommunication serial;
	private SerialConnection serial;

	private Set<ZolertiaListener> listeners;

	// public static Hashtable<String, SensorData> nodeTable = new
	// Hashtable<String, SensorData>();
	public static SensorNode rootNode;

	private String currentTemperature = "";
	private static final String portName = "COM7";

	private BundleContext bundleContext;

	private NetworkManagerApplication networkManager;
	private String myHID;


	public ZolertiaImpl() {

		this.serial = new SerialDumpConnection(new ZolertiaListener() {

			@Override
			public void updateTemperature(String degreesCelsius) {
				currentTemperature = degreesCelsius;
				System.out.println(currentTemperature);
			}

			@Override
			public void serialData(SerialConnection serialConnection,
					String line) {
				handleIncomingData(System.currentTimeMillis(), line);

			}

		});
		this.serial.open(portName);
	}

	public void handleIncomingData(long systemTime, String line) {
		if (line.length() == 0 || line.charAt(0) == '#') {
			// Ignore empty lines, comments, and annotations.
			return;
		}

		System.out.println("SERIAL: " + line);
		SensorData sensorData = SensorData.parseSensorData(line, systemTime);

		// Forçando inicialização do rootNode
		if (rootNode == null) {
			rootNode = new SensorNode();
			rootNode.setId(18);
		}

		if (sensorData != null) {
			int nodeId = sensorData.getNodeID();
			SensorNode node = findNode(rootNode, nodeId);
			int bestNode = (int) Float.parseFloat(sensorData
					.getBestNeighborID());

			if (node == null) {
				node = new SensorNode();
				node.setId(nodeId);
				SensorNode fatherNode = findNode(rootNode, bestNode);
				if (fatherNode != null)
					fatherNode.add(node);
			} else {
				if (!node.getNodeData().getBestNeighborID()
						.equalsIgnoreCase(sensorData.getBestNeighborID())) {
					int oldBestId = (int) Float.parseFloat(node.getNodeData()
							.getBestNeighborID());
					SensorNode oldFatherNode = findNode(rootNode, oldBestId);
					oldFatherNode.removeNode(node.getId() + ".0");
					int newBestId = (int) Float.parseFloat(sensorData
							.getBestNeighborID());
					SensorNode newFatherNode = findNode(rootNode, newBestId);
					newFatherNode.add(node);
				}
			}
			node.setNodeData(sensorData);
			
			return;
		}

	}

	/**
	 * Adds a sensor event listener to the sensor platform. Events are sent
	 * about once per second.
	 * 
	 * @param listener
	 *            the receiver of events
	 */
	public void addListener(ZolertiaListener listener) {
		if (this.listeners == null)
			this.listeners = new HashSet<ZolertiaListener>();

		this.listeners.add(listener);
	}

	private String collectData(SensorNode node) {
		String ans = "";

		SensorNode thisNode = node;
		SensorData sensorData = thisNode.getNodeData();
		if (sensorData != null)
			ans += sensorData.getAllData();

		List<SensorNode> nodes = thisNode.getChildren();
		if (nodes != null && nodes.size() > 0)
			for (SensorNode item : nodes)
				ans += collectData(item);

		return ans;
	}

	private SensorNode findNode(SensorNode node, int findById) {
		SensorNode ans = null;

		SensorNode thisNode = node;
		if (thisNode.getId() == findById)
			ans = thisNode;
		else {
			List<SensorNode> nodes = thisNode.getChildren();
			if (nodes != null && nodes.size() > 0)
				for (SensorNode item : nodes) {
					ans = findNode(item, findById);
					if (ans != null)
						break;
				}
		}

		return ans;
	}

	public void setZolertiasCommand(String command) {
		System.out.println("Enviando comando '" + command + '\'');
		this.serial.writeSerialData(command);
		System.out.println("Comando enviado");
		// return currentTemperature;
		
	}
	
	public String getZolertiasData(){
		String ans = "";
		ans += collectData(rootNode);
		return ans;
	}
}
