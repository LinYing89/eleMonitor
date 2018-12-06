package com.bairock.eleMonitor.data.webData;

import java.util.ArrayList;
import java.util.List;

public class ChartData {

	private Object[][] dataSet;
	private List<ChartSeries> listChartSeries = new ArrayList<>();
	
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
	
}
