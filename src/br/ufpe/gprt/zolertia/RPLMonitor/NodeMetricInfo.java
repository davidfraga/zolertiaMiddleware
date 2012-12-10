package br.ufpe.gprt.zolertia.RPLMonitor;

import java.util.ArrayList;


public class NodeMetricInfo {
	
	private int nodeId;
	private ArrayList<MetricData> nodeMetrics;
	private boolean correctMetric;
	private int counter;
	
	public NodeMetricInfo (int nodeId){
		correctMetric = false;
		counter = 0;
		this.nodeId = nodeId;
		nodeMetrics = new ArrayList<MetricData>();
	}
	
	public int getNodeId (){
		return nodeId;
	}
	
	public ArrayList<MetricData> getNodeMetrics(){
		return nodeMetrics;
	}
	
	public void addNewMetric (MetricData newMetric){
		
		nodeMetrics.add(newMetric);
	}
	
	public int getCounter(){
		return counter;
	}
	
	public void incrementCounter(){
		counter++;
	}
	
	public void resetCounter(){
		counter = 0;
	}
	
	public boolean getCorrectMetric (){
		return correctMetric;
	}
	
	public void setCorrectMetric (boolean correctMetric){
		this.correctMetric = correctMetric;
	}
		
	public void clearMetrics (){
		nodeMetrics.clear();
	}
	
	public void printMetrics (){
		for (MetricData metric : nodeMetrics){
			metric.printMetric();
		}
	}

}
