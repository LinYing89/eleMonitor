package com.bairock.eleMonitor.data.webData;

import java.util.List;

public class DeviceTreeNode {

	private String type;
	private String text;
	private String href;
	private long deviceId;
	private List<DeviceTreeNode> nodes;
	private String[] tags = new String[1];
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public List<DeviceTreeNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<DeviceTreeNode> nodes) {
		this.nodes = nodes;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	
	public void setTag(String tag) {
		tags[0] = tag;
	}
}
