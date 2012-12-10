package br.ufpe.gprt.zolertia.RPLMonitor;

import java.util.ArrayList;
import java.util.HashMap;

public class MetricManager {
	
	public enum SystemState {
		NORMAL_EXECUTION, METRIC_CHECKER, WAITING
	}
	
	private static final String ETX_METRIC = "7";

	private static final String ENERGY_METRIC = "2";

	private static final String ZERO_FUNCTION_METRIC = "0";
	
	private SystemState systemState;
	
	private int counterLimit;
	
	private ArrayList<MetricData> systemMetrics;

	private ArrayList<NodeMetricInfo> nodesMetrics;
	
	public MetricManager() {
		counterLimit = 2;
		systemState = SystemState.NORMAL_EXECUTION;
		systemMetrics = new ArrayList<MetricData>();
		
		nodesMetrics = new ArrayList<NodeMetricInfo>();
	}
	
	//System metrics function
	
	public ArrayList<MetricData> getAtualMetrics() {
		return systemMetrics;
	}
	
	public void clearSystemMetrics(){
		systemMetrics.clear();
	}
	
	public void setSystemState(SystemState systemState){
		this.systemState = systemState;
	}

	public void addSystemMetric(String newMetricInfo) {
		
		MetricData newMetric = selectMetricType(newMetricInfo);
		systemMetrics.add(newMetric);
	}

	// Node metrics function
	
	public void printNodesMetrics(){
		for (NodeMetricInfo node : nodesMetrics){
			node.printMetrics();
		}
	}
	
	public int returnIndexOfSameTypeMetric(NodeMetricInfo node, MetricData newMetric){
		int i = 0;
		for (MetricData nodeMetric : node.getNodeMetrics()){
			if((nodeMetric.getType() == newMetric.getType()) & (nodeMetric.getFlags() == newMetric.getFlags())){
					return i;
			}
			i++;
		}
		return (-1);
	}
	
	
	public ArrayList<NodeMetricInfo> getNodeMetrics(){
		return nodesMetrics;
	}
	
	public void setCounterLimit (int counterLimit){
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

	public void clearNodeMetricsInformation (){
		for ( NodeMetricInfo node : nodesMetrics){
			node.clearMetrics();
			node.resetCounter();
		}
	}
	
	public boolean containNode(int nodeId) {

		for (NodeMetricInfo individualNode : nodesMetrics) {
			if (nodeId == individualNode.getNodeId()) {
				return true;
			}
		}

		return false;

	}
	
	//Metric Manager functions

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

	public void processNewLecture(String newMetricInfo) {
		
		int index = 0;
		String[] fragments = newMetricInfo.split(" ");
		int nodeId = Integer.parseInt(fragments[1]);
		
		MetricData newMetric = selectMetricType(newMetricInfo);

		if (!containNode(nodeId)) {
			
			NodeMetricInfo newNode = new NodeMetricInfo(nodeId);
			newNode.addNewMetric(newMetric);
			nodesMetrics.add(newNode);
			
		} else {
			
			for (NodeMetricInfo nodes : nodesMetrics) {
				if (nodes.getNodeId() == nodeId) {
		// Caution !!	
					
					index = returnIndexOfSameTypeMetric(nodes, newMetric);			
					if(index != (-1)){
						nodes.getNodeMetrics().remove(index);
					}
					
					nodes.addNewMetric(newMetric);
					
					if(systemState == SystemState.WAITING){
						nodes.incrementCounter();
					}
					break;
				}
			}
		}
		
		
	}

	public boolean metricCheck() {

		setSystemState(SystemState.METRIC_CHECKER);
		System.out.println("Metric check was started.");
		boolean correctMetric = true;
		boolean readyToRepair = true;

		for (NodeMetricInfo node : nodesMetrics) {

			correctMetric = true;
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

			node.setCorrectMetric(correctMetric);
		}
		
		for (NodeMetricInfo node : nodesMetrics) {
			if (!node.getCorrectMetric()) {
				readyToRepair = false;
				
				if(node.getCounter() >= counterLimit)
				{
					// TODO Reenviar Comando
					System.out.println("Limit reached... comand sent again");
					//node.clearMetrics();
					node.resetCounter();
				}
				
			}
		}
		
		if(!readyToRepair){
			setSystemState(SystemState.WAITING);
			System.out.println("System in wait mode. ");
		}
		else {
			setSystemState(SystemState.NORMAL_EXECUTION);
			System.out.println("System in normal mode of operation. ");
			clearNodeMetricsInformation();

		}

		return readyToRepair;
	}

}
