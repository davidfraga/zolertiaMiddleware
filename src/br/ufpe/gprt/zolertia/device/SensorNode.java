package br.ufpe.gprt.zolertia.device;

import java.util.ArrayList;
import java.util.List;

public class SensorNode {

	private int id;
	private String name;
	private List<SensorNode> children;
	private SensorData nodeData;
	private long lastTimeRead;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SensorNode> getChildren() {
		return children;
	}

	public void setChildren(List<SensorNode> children) {
		this.children = children;
	}

	public SensorData getNodeData() {
		return nodeData;
	}

	public void setNodeData(SensorData nodeData) {
		this.nodeData = nodeData;
	}

	public void add(SensorNode node) {
		if (this.children == null)
			this.children = new ArrayList<SensorNode>();
		this.children.add(node);
	}

	public boolean removeNode(String nodeID) {
		boolean ans = true;
		if (this.children == null)
			ans = false;
		else {
			for (SensorNode item : children) {
				if (item.getId() == Integer.parseInt(nodeID)) {
					ans = children.remove(item);
					break;
				}
			}
		}
		return ans;
	}

	public long getLastTimeRead() { 
		return this.lastTimeRead;
	}

}
