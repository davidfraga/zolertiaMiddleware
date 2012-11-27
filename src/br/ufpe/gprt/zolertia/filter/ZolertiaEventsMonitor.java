package br.ufpe.gprt.zolertia.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javatuples.Pair;

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
public class ZolertiaEventsMonitor extends Thread {

	private int loop = 3000;
	private ZolertiaData zolertia;
	private boolean running = false;

	public ZolertiaEventsMonitor(ZolertiaData zolertia, int loop) {
		this.zolertia = zolertia;
		this.loop = loop;
	}

	public void run() {
		running = true;
		while (running) {
			if (zolertia.getUnreadSensorsData().size() > 0) {
				try {
					for (SensorData sd : zolertia.getUnreadSensorsData()) {
						for (ActiveContext context : ResourceManager
								.getInstance().getContextManager()
								.getActiveContexts()) {
							boolean inContext = true;
							List<Pair<Enum_DataType, Double>> dataTypes = new ArrayList<Pair<Enum_DataType,Double>>();
							for (Policy policy : context.getPolicies()) {
								Pair<Pair<Enum_DataType, Double>, Pair<Enum_DataType, Double>> types = ResourceManager
										.getInstance().getPolicyManager()
										.policyEvaluation(policy, sd);
								
								if (types.getValue0() == null) {
									inContext = false;
									dataTypes.add(types.getValue0());
								} else
									dataTypes.add(types.getValue1());

								if (inContext)
									System.out.println("IN CONTEXT TO "
											+ context.getTopic());
								else
									System.out.println("OUT OF CONTEXT TO "
											+ context.getTopic());

								ResourceManager.getInstance()
										.publishToSubscriber(context,
												dataTypes, inContext);
							}
						}
						try {
							Thread.sleep(loop);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (java.util.ConcurrentModificationException ex) {
					System.out.println("ZEP exception: " + ex.getMessage());
				}
				zolertia.clearSensorData();
			}
		}

	}

	public void stopReading() {
		running = false;
	}

}
