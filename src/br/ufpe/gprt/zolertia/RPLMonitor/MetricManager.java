package br.ufpe.gprt.zolertia.RPLMonitor;

import java.util.ArrayList;

import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.zolertia.deviceCommandProxy.CommandFormat;

public class MetricManager {

	private static final String ETX_METRIC = "7";

	private static final String ENERGY_METRIC = "2";

	private static final String ZERO_FUNCTION_METRIC = "0";

	private int counterLimit;

	private int nodesNumber;

	private boolean readyToRepair;

	private ArrayList<MetricData> systemMetrics;

	private ArrayList<NodeMetricInfo> correctNodes;
	private ArrayList<NodeMetricInfo> wrongNodes;

	public MetricManager() {
		readyToRepair = false;
		counterLimit = 2;
		systemMetrics = new ArrayList<MetricData>();
		correctNodes = new ArrayList<NodeMetricInfo>();
		wrongNodes = new ArrayList<NodeMetricInfo>();
	}

	// System metrics function
	public boolean getReadyToRepair() {
		return readyToRepair;
	}

	public ArrayList<MetricData> getAtualMetrics() {
		return systemMetrics;
	}

	public void clearSystemMetrics() {
		systemMetrics.clear();
	}

	public void setNodesNumber(int value) {
		nodesNumber = value;
	}

	public void addSystemMetric(String newMetricInfo) {

		MetricData newMetric = selectMetricType(newMetricInfo);
		systemMetrics.add(newMetric);
	}

	
	public int returnIndexOfSameTypeMetric(NodeMetricInfo node,
			MetricData newMetric) {
		int i = 0;
		for (MetricData nodeMetric : node.getNodeMetrics()) {
			if ((nodeMetric.getType() == newMetric.getType())
					& (nodeMetric.getFlags() == newMetric.getFlags())) {
				return i;
			}
			i++;
		}
		return (-1);
	}

	public ArrayList<NodeMetricInfo> getCorrectNodes() {
		return correctNodes;
	}

	public ArrayList<NodeMetricInfo> getWrongNodes() {
		return wrongNodes;
	}

	public void setCounterLimit(int counterLimit) {
		this.counterLimit = counterLimit;
	}

	public boolean containMetric(MetricData systemMetric, NodeMetricInfo node) {

		for (MetricData nodeMetric : node.getNodeMetrics()) {

			if (nodeMetric.compare(systemMetric)) {
				return true;
			}
		}
		return false;

	}

	public void clearNodeMetricsInformation() {
		for (NodeMetricInfo correctNode : correctNodes) {
			correctNode.clearMetrics();
			correctNode.resetCounter();
			wrongNodes.add(correctNode);
		}
		correctNodes.clear();

	}

	public boolean correctNodescontainNode(int nodeId) {

		for (NodeMetricInfo individualNode : correctNodes) {
			if (nodeId == individualNode.getNodeId()) {
				return true;
			}
		}

		return false;

	}

	public boolean wrongNodescontainNode(int nodeId) {

		for (NodeMetricInfo individualNode : wrongNodes) {
			if (nodeId == individualNode.getNodeId()) {
				return true;
			}
		}

		return false;

	}

	// Metric Manager functions

	public MetricData selectMetricType(String newMetricInfo) {

		String[] fragments = newMetricInfo.split(" ");

		MetricData noMetricType = null;
		if (fragments[2].equals(ETX_METRIC)) {

			MetricData newMetric = new EtxMetric(fragments);
			return newMetric;
		}
		if (fragments[2].equals(ENERGY_METRIC)) {

			MetricData newMetric = new EnergyMetric(fragments);
			return newMetric;
		}
		if (fragments[2].equals(ZERO_FUNCTION_METRIC)) {

			MetricData newMetric = new ZeroFunction(fragments);
			return newMetric;
		}
		return noMetricType;
	}

	public boolean checkNodeMetrics(NodeMetricInfo node) {

		boolean correctMetric = true;
		if (node.getNodeMetrics().size() == systemMetrics.size()) {

			for (MetricData systemMetric : systemMetrics) {

				if (!containMetric(systemMetric, node)) {
					correctMetric = false;
					break;
				}
			}
		}

		else {
			correctMetric = false;
		}
		return correctMetric;
	}

	public void eliminateWrongNodes() {

		for (NodeMetricInfo correctNode : correctNodes) {
			wrongNodes.remove(correctNode);
		}
	}

	public void processNewLecture(String newMetricInfo) {

		boolean haveNewCorrectNodes = false;
		int index = 0;
		String[] fragments = newMetricInfo.split(" ");
		int nodeId = Integer.parseInt(fragments[1]);

		MetricData newMetric = selectMetricType(newMetricInfo);

		newMetric.printMetric();

		if (!correctNodescontainNode(nodeId)) {
			if (!wrongNodescontainNode(nodeId)) {

				NodeMetricInfo newNode = new NodeMetricInfo(nodeId);
				newNode.addNewMetric(newMetric);
				wrongNodes.add(newNode);

				boolean checkCounter = false;
				haveNewCorrectNodes = analizeNode(newNode, checkCounter);
				
				/*if (checkNodeMetrics(newNode)) {
					haveNewCorrectNodes = true;
					correctNodes.add(newNode);
				}

				else {
					readyToRepair = false;
					if (newNode.getNodeMetrics().size() == systemMetrics.size()) {
						newNode.incrementCounter();
						
					}
				}*/
			}

			else {
				for (NodeMetricInfo nodes : wrongNodes) {
					if (nodes.getNodeId() == nodeId) {
						// Caution !!

						if (!containMetric(newMetric, nodes)) {

							index = returnIndexOfSameTypeMetric(nodes,
									newMetric);
							if (index != (-1)) {
								nodes.getNodeMetrics().remove(index);
							}
							nodes.addNewMetric(newMetric);
						}

						boolean checkCounter = true;
						haveNewCorrectNodes = analizeNode(nodes, checkCounter);
						
						/*if (checkNodeMetrics(nodes)) {
								haveNewCorrectNodes = true;
							correctNodes.add(nodes);
						}

						else {
							readyToRepair = false;
							if (nodes.getNodeMetrics().size() == systemMetrics
									.size()) {
								nodes.incrementCounter();

								if (nodes.getCounter() >= counterLimit) {
									// Reenviar Comando...unitario ou em grupo ?
										resetAllNodeCounters();
								}
							}
						}*/
						break;
					}
				}
			}
			if (haveNewCorrectNodes) {
				eliminateWrongNodes();
				metricCheck();
			}

		}
	}

	private boolean analizeNode(NodeMetricInfo node, boolean checkCounter) {
		boolean result = false;

		if (checkNodeMetrics(node)) {
			result = true;
			correctNodes.add(node);
		}

		else {
			readyToRepair = false;
			if (node.getNodeMetrics().size() == systemMetrics.size()) {
				node.incrementCounter();
				if (checkCounter){
					if (node.getCounter() >= counterLimit) {
						// Reenviar Comando...unitario ou em grupo ?
						String command = CommandFormat.getRPLCommand(node.getNodeId(), systemMetrics);
						if (command!=null) ResourceManager.getInstance().getZolertiaData().sendZolertiaCommand(command);
						resetAllNodeCounters();
					}
				}
				
			}
		}
		
		
		return result;
	}

	public void resetAllNodeCounters() {
		for (NodeMetricInfo wrongNode : wrongNodes) {
			wrongNode.resetCounter();
		}
	}

	public void resetReadyToRepair() {
		readyToRepair = false;
	}

	public void metricCheck() {

		System.out.println("Metric check was started.");

		if ((wrongNodes.size() == 0) & (correctNodes.size() > 0)) {
			readyToRepair = true;
			System.out.println("System Ready to Repair");
			clearNodeMetricsInformation();
		} else {
			System.out.println("System not Ready to Repair");
		}

	}

}
