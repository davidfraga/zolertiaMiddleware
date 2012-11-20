package br.ufpe.gprt.test;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Vector;

import br.ufpe.gprt.eventmanager.Subscription;
import br.ufpe.gprt.eventmanager.impl.EventManager;
import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.semantic.ActiveContext;
import br.ufpe.gprt.semantic.Context;
import br.ufpe.gprt.semantic.ContextManager;
import br.ufpe.gprt.semantic.Policy;
import br.ufpe.gprt.zolertia.device.SensorData;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Client side				
		EventManager eventManager = new EventManager();
		String context1 = "human_body_temperature";
		String endpoint1 = "192.168.0.191:9095";
		String context2 = "Cold";
		String endPoint2 = "255.0.245.1:9090";
		String endpoint3 = "0.0.0.0:9090";
		
		try {
			eventManager.subscribe(context1, endpoint1);
//			eventManager.subscribe(context2, endPoint2);
//			eventManager.subscribe(context2, endpoint3);
//			eventManager.subscribe(context1, endpoint3);

			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//System.out.println(ResourceManager.getInstance().getContextManager().getActiveContexts().size());
				 
		// Zolertia side
//		String line = "30 0 23 0 114 200 1 0 22 13700 0 333 32816 23 167 117 8 512 1 32 0 0 0 0 6336 0 0 0 0 0"; 
//		ResourceManager.getInstance().getZolertiaData().handleIncomingData(System.currentTimeMillis(), line);
		
								
	}
		
		
		
	}


