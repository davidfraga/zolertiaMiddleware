package br.ufpe.gprt.zolertia.impl;

import java.util.List;

import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.semantic.Context;

public class ZolertiaEvents extends Thread {


	private int loop = 0;
	private ZolertiaImpl zolertia;
	
	
	public ZolertiaEvents(ZolertiaImpl zolertia, int loop) {
		this.zolertia = zolertia;
		this.loop = loop;
	}

	public void run() {
		while (true) {
/*				for (Context item : ResourceManager.getInstance().getA()) {
					
				} */
			try {
				Thread.sleep(loop);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
}
