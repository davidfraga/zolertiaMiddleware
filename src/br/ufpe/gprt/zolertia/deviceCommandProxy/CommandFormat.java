package br.ufpe.gprt.zolertia.deviceCommandProxy;


import java.util.ArrayList;
import java.util.List;

import br.ufpe.gprt.semantic.ContextManager.ActionTypeRelatedToCondition;
import br.ufpe.gprt.semantic.ContextManager.Enum_Action;
import br.ufpe.gprt.semantic.Policy;
import br.ufpe.gprt.zolertia.RPLMonitor.EnergyMetric;
import br.ufpe.gprt.zolertia.RPLMonitor.EtxMetric;
import br.ufpe.gprt.zolertia.RPLMonitor.MetricData;
import br.ufpe.gprt.zolertia.RPLMonitor.ZeroFunction;

public class CommandFormat {
	
	private static final String METRIC_HEADER = "udpcmd6 aaa::c30c:0000:0000:";
	
	public enum CommandType{
		INSERT, DELETE
	}
	
	public static String getPeriodCommand(Enum_Action action){
		String result = "netcmd {";
		switch (action) {
		case SEND_PACKETS_LESS_FREQUENTLY:
			result +="period+}";
			break;
		case SEND_PACKETS_MORE_FREQUENTLY:
			result +="period-}";
			break;

		default:
			break;
		}
		
		return result;
	}

	public static String getRPLCommand(int nodeId,
			ArrayList<MetricData> systemMetrics) {
		String id = nodeId+"";
		if (nodeId<10) id="000"+id;
		else if (nodeId<100) id = "00"+id;
			
		String command = METRIC_HEADER+id+" ";
		
		for (MetricData metricData : systemMetrics) {
			if (metricData instanceof EnergyMetric){
				String estimation = "0";
				command+="setRplEnergyMetric "+metricData.getFlags()+" "+metricData.getPrec()+" "+estimation; // ID?
			} else if (metricData instanceof ZeroFunction){
				int constraint = 0;
				command+="setRpl0Metric "+constraint+" "+metricData.getPrec(); // ID?
			} else if (metricData instanceof EtxMetric){
				int etx = ((EtxMetric)metricData).getEtx();
				command+="setRplEtxMetric "+metricData.getFlags()+" "+metricData.getPrec()+" "+etx; // ID?
			}
			
		}				
		
		return command;
	}
}
