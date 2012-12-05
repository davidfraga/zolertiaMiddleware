package br.ufpe.gprt.zolertia.impl;

//import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufpe.gprt.zolertia.device.SensorData;
import br.ufpe.gprt.zolertia.device.SensorNode;
import br.ufpe.gprt.zolertia.deviceCommandProxy.SerialConnection;
import br.ufpe.gprt.zolertia.deviceCommandProxy.SerialDumpConnection;
import br.ufpe.gprt.zolertia.filter.Garbagge;
import br.ufpe.gprt.zolertia.filter.ZolertiaEventsMonitor;

/**
 * Class that has all resources of zolertia network, including nodes (topology),
 * sensor data, commands to send, activation of garbagge and processing threads
 * 
 * @author GPRT-BEMO
 * 
 */
public class ZolertiaData {

	private static final int READING_LOOP = 3000;

	private static final int ROOT_ID = 17;

	private SerialConnection serial;

	private Set<ZolertiaListener> listeners;

	public SensorNode rootNode;

	private static String portName;

	private ArrayList<SensorData> sensorsData;

	private Garbagge garbagge;

	private ZolertiaEventsMonitor eventsProcessing;

	public ZolertiaData() {

		portName = "COM12";
		System.out.println("Zolertia port: " + portName);
		this.serial = new SerialDumpConnection(new ZolertiaListener() {

			@Override
			public void serialData(SerialConnection serialConnection,
					String line) {
				handleIncomingData(System.currentTimeMillis(), line);
				System.out.println("Reading: " + line);
			}

		});
		this.serial.open(portName);

		sensorsData = new ArrayList<SensorData>();

		garbagge = new Garbagge(this);
		garbagge.start();

		eventsProcessing = new ZolertiaEventsMonitor(this, READING_LOOP);
		eventsProcessing.start();

		//setZolertiasCommand("reboot");

		// Forçando inicialização do rootNode

		rootNode = new SensorNode();
		rootNode.setId(ROOT_ID);

	}

	public synchronized void handleIncomingData(long systemTime, String line) {
		if (line.length() == 0 || line.charAt(0) == '#') {
			// Ignore empty lines, comments, and annotations.
			return;
		}

		// TODO verificar se o dado é de sensor ou de controle
		// if (sensor){
		SensorData sensorData = SensorData.parseSensorData(line, systemTime);

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
			synchronized (this) {
				this.sensorsData.add(sensorData);
			}
			
		}
		// TODO if (controle){
		// verificar se é do RPL. Caso positivo chamar o metricCheck
		// verificar se são os canais. Caso positivo, chamar o radioManager
		// }

	}

	public synchronized ArrayList<SensorData> getUnreadSensorsData() {

		return sensorsData;
	}

	public synchronized void clearSensorData() {
		sensorsData.clear();
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

	/*
	 * public void createCommand(Policy policy) { String command =
	 * policy.getDataType
	 * ().name()+","+policy.getCondition().name()+","+policy.getConditionParam
	 * ()+","+policy.getAction().name(); setZolertiasCommand(command); }
	 */

	public void sendZolertiaCommand(String command) {
		System.out.println("Enviando comando '" + command + '\'');
		this.serial.writeSerialData(command);
		System.out.println("Comando enviado");
	}

//	public void stopCommand(Policy policy) {
//		String command = policy.getDataType().name() + ","
//				+ policy.getCondition().name() + ","
//				+ policy.getConditionParam() + "," + Enum_Action.STOP_POLICY;
//		sendZolertiaCommand(command);
//	}

}
