package br.ufpe.gprt.zolertia.RPLMonitor;


public class EnergyMetric extends MetricData {
	
	private int subFlags;
	private int energy_est;
	private int tlv_type;
	private int tlv_length;
	private int tlv_value;

	int getSubFlags(){
		return subFlags;
	}
	int getEnergyEst(){
		return energy_est;
	}
	int getTlvType(){
		return tlv_type;
	}
	int getTlvLength(){
		return tlv_length;
	}
	int getTlvValue(){
		return tlv_value;
	}
	
	EnergyMetric(String[] fragments) {
		
		super(fragments);
		
		subFlags = Integer.parseInt(fragments[7]);
		energy_est = Integer.parseInt(fragments[8]);
		tlv_type = Integer.parseInt(fragments[9]);
		tlv_length = Integer.parseInt(fragments[10]);
		tlv_value = Integer.parseInt(fragments[11]);
		
	}
	
	void printMetric (){
		
		System.out.println("Node " + this.getNodeId() + "Metric Energy : "+
						   " " + this.getFlags() +
						   " " + this.getAggr() +
						   " " + this.getPrec() +
						   " " + this.getLength() +
						   " " + this.getSubFlags() +
						   " " + this.getEnergyEst() +
						   " " + this.getTlvType() +
						   " " + this.getTlvLength() +
						   " " + this.getTlvValue());
	}
	
	boolean compare(MetricData metric){
		
		if(metric instanceof EnergyMetric){
			EnergyMetric energyMetric = (EnergyMetric)metric;
			
			return  (this.getFlags() == energyMetric.getFlags()) &
					(this.getAggr() == energyMetric.getAggr()) &
					(this.getPrec() == energyMetric.getPrec()) &
					(this.getLength() == energyMetric.getLength()) &
					(this.getSubFlags() == energyMetric.getSubFlags())&
					(this.getEnergyEst() == energyMetric.getEnergyEst())&
					(this.getTlvType() == energyMetric.getTlvType()) &
					(this.getTlvLength() == energyMetric.getTlvLength()) &
					(this.getTlvValue() == energyMetric.getTlvValue());
					
		}
		return false;
	}
}
