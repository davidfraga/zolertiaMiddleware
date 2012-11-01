package br.ufpe.gprt.zolertia.filter;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.gprt.eventmanager.Part;
import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.semantic.Context;
import br.ufpe.gprt.semantic.Policy;
import br.ufpe.gprt.zolertia.device.SensorData;
import br.ufpe.gprt.zolertia.impl.ZolertiaData;

/**
 * Publish sensors data when there is any unread data and when this data satisfies a context
 * @author GPRT-BEMO
 *
 */
public class ZolertiaEventsProcessing extends Thread{


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
			for (SensorData sd : zolertia.getUnreadSensorsData()) {
					for (Context context : ResourceManager.getInstance().getContextManager().getActiveContexts()){
						List<Part> parts = new ArrayList<Part>();
						for (Policy policy : context.getPolicies()){
							boolean policyWithAction = false;
							Part part = ResourceManager.getInstance().getPolicyManager().policyEvaluation(policy, sd, policyWithAction);
							if (part != null) parts.add(part);
							else {
								parts.clear();
								break;
							}
							
						}
						
						if (parts.size()>0){
							try {
								ResourceManager.getInstance().publish(context.getTopic(), (Part[])parts.toArray());
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			try {
				Thread.sleep(loop);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopReading(){
		running = false;
	}

	
}
