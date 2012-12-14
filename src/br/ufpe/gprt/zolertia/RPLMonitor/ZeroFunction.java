package br.ufpe.gprt.zolertia.RPLMonitor;


public class ZeroFunction extends MetricData {

	public ZeroFunction(String[] fragments) {
		super(fragments);
	}
	
	public void printMetric (){
			
			System.out.println("Node " + this.getNodeId() + "Metric zeroFunction : "+
							   " " + this.getFlags() +
							   " " + this.getAggr() +
							   " " + this.getPrec() +
							   " " + this.getLength());
		}
	
	public boolean compare(MetricData metric){

		if(metric instanceof ZeroFunction){
			ZeroFunction zeroFunction = (ZeroFunction)metric;
			
			return (this.getFlags() == zeroFunction.getFlags()) &
				   (this.getAggr() == zeroFunction.getAggr()) &
				   (this.getPrec() == zeroFunction.getPrec()) &
				   (this.getLength() == zeroFunction.getLength());
						
		}

		return false;
	}
	
	
}
