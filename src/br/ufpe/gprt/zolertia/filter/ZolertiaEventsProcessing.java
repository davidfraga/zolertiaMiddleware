package br.ufpe.gprt.zolertia.filter;

import java.util.HashMap;
import java.util.Map;

import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.semantic.ActiveContext;
import br.ufpe.gprt.semantic.Policy;
import br.ufpe.gprt.semantic.PolicyManager.Enum_DataType;
import br.ufpe.gprt.zolertia.device.SensorData;
import br.ufpe.gprt.zolertia.impl.ZolertiaData;

/**
 * Publish sensors data when there is any unread data and when this data
 * satisfies a context
 * 
 * @author GPRT-BEMO
 * 
 */
public class ZolertiaEventsProcessing extends Thread {

	private int loop = 3000;
	private ZolertiaData zolertia;
	private boolean running = false;
	

	public ZolertiaEventsProcessing(ZolertiaData zolertia, int loop) {
		this.zolertia = zolertia;
		this.loop = loop;
	}

	public void run() {
		running = true;
		while (running) {
			if (zolertia.getUnreadSensorsData().size() > 0) {
				for (SensorData sd : zolertia.getUnreadSensorsData()) {
					for (ActiveContext context : ResourceManager.getInstance()
							.getContextManager().getActiveContexts()) {
						// System.out.println(context.getTopic());

						boolean inContext = true;
						Map<Enum_DataType, Double> dataTypes = new HashMap<Enum_DataType,Double>();
						for (Policy policy : context.getPolicies()) {
							boolean policyWithAction = true;
							Map<Enum_DataType, Double> types = ResourceManager
									.getInstance()
									.getPolicyManager()
									.policyEvaluation(policy, sd,
											policyWithAction);
							if (types.size()==0) {
								inContext = false;
								dataTypes.clear();
								break;
							} else dataTypes.putAll(types);
						}
						if (inContext) {
							System.out.println("IN CONTEXT: "+context.getTopic());
							ResourceManager.getInstance().publishToSubscriber(
									context, inContext, dataTypes);
						} else System.out.println("OUT OF CONTEXT: "+context.getTopic());

					}
				}
				zolertia.clearSensorData();
			}
			try {
				Thread.sleep(loop);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopReading() {
		running = false;
	}

}
