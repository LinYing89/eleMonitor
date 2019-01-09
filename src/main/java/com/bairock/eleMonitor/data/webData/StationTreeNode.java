package com.bairock.eleMonitor.data.webData;

public class StationTreeNode extends DeviceTreeNode {

	private double lat;
	private double lng;
	private int stateCode;
	
	public double getLat() {
		return lat;
	}
	public void setLat(double d) {
		this.lat = d;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public int getStateCode() {
		return stateCode;
	}
	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}
	
}
