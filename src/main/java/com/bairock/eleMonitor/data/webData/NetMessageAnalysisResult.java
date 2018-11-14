package com.bairock.eleMonitor.data.webData;

import java.util.ArrayList;
import java.util.List;

public class NetMessageAnalysisResult {

	private String time = "";
	private String data = "";

	private List<AnalysisReceivedErrorResult> listErrorResult = new ArrayList<>();

	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public List<AnalysisReceivedErrorResult> getListErrorResult() {
		return listErrorResult;
	}
	public void setListErrorResult(List<AnalysisReceivedErrorResult> listErrorResult) {
		this.listErrorResult = listErrorResult;
	}
	
	public void addErrResult(AnalysisReceivedErrorResult err) {
		listErrorResult.add(err);
	}
}
