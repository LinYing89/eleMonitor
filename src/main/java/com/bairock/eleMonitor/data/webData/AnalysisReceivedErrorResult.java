package com.bairock.eleMonitor.data.webData;

import java.util.ArrayList;
import java.util.List;

public class AnalysisReceivedErrorResult {

	private int code = 0;
	private String data = "";
	private String message = "";
	private String head = "";
	private List<String> listOne = new ArrayList<>();
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public List<String> getListOne() {
		return listOne;
	}
	public void setListOne(List<String> listOne) {
		this.listOne = listOne;
	}
	
}
