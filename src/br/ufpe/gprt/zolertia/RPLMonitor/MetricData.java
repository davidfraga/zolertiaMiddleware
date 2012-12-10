package br.ufpe.gprt.zolertia.RPLMonitor;


public abstract class MetricData {
	private int nodeId;
	private int type;
	private int flags;
	private int aggr;
	private int prec;
	private int length;
	
	int getType(){
		return type;
	}
	int getFlags(){
		return flags;
	}
	int getAggr(){
		return aggr;
	}
	int getPrec (){
		return prec;
	}
	int getLength(){
		return length;
	}
	int getNodeId(){
		return nodeId;
	}

	abstract void printMetric();	
	abstract boolean compare(MetricData metric);
	
	public MetricData (String[] fragments){
		
		nodeId = Integer.parseInt(fragments[1]);
		type = Integer.parseInt(fragments[2]);
		flags = Integer.parseInt(fragments[3]);
		aggr = Integer.parseInt(fragments[4]);
		prec = Integer.parseInt(fragments[5]);
		length = Integer.parseInt(fragments[6]);
	}
	
		
}