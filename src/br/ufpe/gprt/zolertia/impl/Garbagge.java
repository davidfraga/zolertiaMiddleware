package br.ufpe.gprt.zolertia.impl;

import java.util.List;

public class Garbagge extends Thread {


	private final int TIME_EXPIRED = 60000;
	private final int TIME_LOOP = 10000;
	private static boolean isRemoved = false;
	
	
	public Garbagge() {

	}

	public void run() {
		while (true) {
			if (ZolertiaImpl.rootNode != null)
				if (ZolertiaImpl.rootNode.getChildren() != null
					|| ZolertiaImpl.rootNode.getChildren().size() > 0) {
				depthRun(ZolertiaImpl.rootNode, ZolertiaImpl.rootNode);
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
