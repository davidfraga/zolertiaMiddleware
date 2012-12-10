package br.ufpe.gprt.zolertia.RPLMonitor;


public class Main {
	
	public static void main (String args []){
		String string1 = "2 1 2 3 4 5 6 7 8 9 10 11";
		String string1c = "2 1 2 3 4 5 6 7 8 9 10 0";
		String string2 = "2 2 2 3 4 5 6 7 8 9 10 11";
		String string3 = "2 2 2 3 4 5 6 7 8 9 10 0";
		MetricManager metricManager = new MetricManager();
				
		metricManager.addSystemMetric(string3);

		
		metricManager.processNewLecture(string2);
		System.out.println(metricManager.metricCheck());
		metricManager.printNodesMetrics();
		
		metricManager.processNewLecture(string2);
		System.out.println(metricManager.metricCheck());
		
		
		metricManager.processNewLecture(string1);
		System.out.println(metricManager.metricCheck());
		metricManager.printNodesMetrics();
		
				
		metricManager.processNewLecture(string3);
		metricManager.processNewLecture(string1c);
		System.out.println(metricManager.metricCheck());
		metricManager.printNodesMetrics();
	
		
	}

}
