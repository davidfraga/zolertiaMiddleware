package br.ufpe.gprt.zolertia.filter;

import java.util.List;

import br.ufpe.gprt.zolertia.device.SensorNode;
import br.ufpe.gprt.zolertia.impl.ZolertiaData;

/**
 * Evaluate the stored sensor data. If the difference from the sensor last reading and 
 * the time 'NOW' is greater than a 'TIME_EXPIRED' value, remove this node
 * @author GPRT-BEMO
 *
 */
public class Garbagge extends Thread {


	private final int TIME_EXPIRED = 60000;
	private final int TIME_LOOP = 10000;
	private ZolertiaData zolertiaData;
	private static boolean isRemoved = false;
	
	
	public Garbagge(ZolertiaData zolertiaData) {
		this.zolertiaData = zolertiaData;
	}

	public void run() {
		while (true) {
			if (zolertiaData.rootNode != null)
				if (zolertiaData.rootNode.getChildren() != null
					|| zolertiaData.rootNode.getChildren().size() > 0) {
				depthRun(zolertiaData.rootNode, zolertiaData.rootNode);
			}
			try {
				Thread.sleep(TIME_LOOP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void depthRun(SensorNode fathernode, SensorNode childNode) {
		if (childNode.getChildren() == null
				|| childNode.getChildren().size() <= 0) {
			if (!fathernode.equals(childNode)) {
				long time = System.currentTimeMillis();
				long timeDifference = time - childNode.getLastTimeRead();
				String id = childNode.getId()+".0";
				System.out.println("Node '"+ id +"' was updated in "+(timeDifference/1000.0)+" seconds ago");
				if (timeDifference > TIME_EXPIRED){
					
					fathernode.removeNode(id);
					System.out.println("Node '"+ id +"' removed");
				}
			}
		} else {
			List<SensorNode> nodes = childNode.getChildren();
			for (int i=0; i<nodes.size(); i++)
				if (!isRemoved) depthRun(childNode, nodes.get(i));
				else {
					depthRun(childNode, nodes.get(i-1));
					isRemoved = false;
				}
			
		}
	}
}
