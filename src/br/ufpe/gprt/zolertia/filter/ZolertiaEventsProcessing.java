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
						boolean contextSatisfied = true;
						for (Policy policy : context.getPolicies()){
							if (!ResourceManager.getInstance().getPolicyManager().policyCheck(policy, sd)){
								contextSatisfied = false;
								break;
							}
						}
						
						if (contextSatisfied) {
							List<Part> parts = new ArrayList<Part>();
							for (Policy policy : context.getPolicies()){
								//policy.getDataType()
							}
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
