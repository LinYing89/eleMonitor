package com.bairock.eleMonitor.data.webData;

import java.util.ArrayList;
import java.util.List;

public class ChartData {

	private Object[][] dataSet;
	private List<ChartSeries> listChartSeries = new ArrayList<>();
	private boolean ended;
	
	public Object[][] getDataSet() {
		return dataSet;
	}
	public void setDataSet(Object[][] dataSet) {
		this.dataSet = dataSet;
	}
	public List<ChartSeries> getListChartSeries() {
		return listChartSeries;
	}
	public void setListChartSeries(List<ChartSeries> listChartSeries) {
		this.listChartSeries = listChartSeries;
	}
	public boolean isEnded() {
		return ended;
	}
	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
}
