package com.bairock.eleMonitor.data.webData;

public class StationStateData {

	private long stationId;
	private String stationName;
	private int code;
	private String message;

	public StationStateData() {
		
	}
	
	public StationStateData(long stationId, String stationName, int code, String message) {
		this.stationId = stationId;
		this.stationName = stationName;
		this.code = code;
		this.message = message;
	}
	
	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
