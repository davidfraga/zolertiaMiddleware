package br.ufpe.gprt.zolertia.RPLMonitor;


public class EtxMetric extends MetricData{
	
		private int etx;
		
		public EtxMetric (String[] fragments){
			super(fragments);
			
			etx = Integer.parseInt(fragments[7]);
		}
		
		
		public void printMetric (){
			
			System.out.println("Node " + this.getNodeId() + "Metric Etx : "+
							   " " + this.getFlags() +
							   " " + this.getAggr() +
							   " " + this.getPrec() +
							   " " + this.getLength() +
							   " " + this.getEtx());
		}

		public boolean compare(MetricData metric){
		
			if(metric instanceof EtxMetric){
			
			EtxMetric etxMetric = (EtxMetric)metric;
				
			return (this.getFlags() == etxMetric.getFlags()) &
					(this.getAggr() == etxMetric.getAggr()) &
					(this.getPrec() == etxMetric.getPrec()) &
					(this.getLength() == etxMetric.getLength())&
					(this.getEtx() == etxMetric.getEtx());
		}
		
		return false;
			
		}
		
		public int getEtx(){
			return etx;
		}

		

}
