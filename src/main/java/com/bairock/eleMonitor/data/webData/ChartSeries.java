package com.bairock.eleMonitor.data.webData;

public class ChartSeries {

	private String type;
	private String seriesLayoutBy;
	
	public ChartSeries() {
		type = "line";
		seriesLayoutBy = "row";
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSeriesLayoutBy() {
		return seriesLayoutBy;
	}
	public void setSeriesLayoutBy(String seriesLayoutBy) {
		this.seriesLayoutBy = seriesLayoutBy;
	}
	
	
}
