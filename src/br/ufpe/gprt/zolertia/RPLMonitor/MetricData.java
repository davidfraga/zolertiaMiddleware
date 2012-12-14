package br.ufpe.gprt.zolertia.RPLMonitor;


public abstract class MetricData {
	private int nodeId;
	private int type;
	private int flags;
	private int aggr;
	private int prec;
	private int length;
	
	public int getType(){
		return type;
	}
	public int getFlags(){
		return flags;
	}
	public int getAggr(){
		return aggr;
	}
	public int getPrec (){
		return prec;
	}
	public int getLength(){
		return length;
	}
	public int getNodeId(){
		return nodeId;
	}

	public abstract void printMetric();	
	public abstract boolean compare(MetricData metric);
	
	public MetricData (String[] fragments){
		
		nodeId = Integer.parseInt(fragments[1]);
		type = Integer.parseInt(fragments[2]);
		flags = Integer.parseInt(fragments[3]);
		aggr = Integer.parseInt(fragments[4]);
		prec = Integer.parseInt(fragments[5]);
		length = Integer.parseInt(fragments[6]);
	}
	
		
}