package com.bairock.eleMonitor.data.webData;

import com.bairock.eleMonitor.data.ValueType;

public class DevWebEleData extends DevWebData{
	private String phaseNum;
	
	public DevWebEleData() {
		setValueType(ValueType.ELE);
	}
	public String getPhaseNum() {
		return phaseNum;
	}
	public void setPhaseNum(String phaseNum) {
		this.phaseNum = phaseNum;
	}
	
	
}
