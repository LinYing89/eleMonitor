package com.bairock.eleMonitor.data.webData;

import com.bairock.eleMonitor.data.ValueType;

/**
 * 发往网页的bean
 * @author 44489
 *
 */
public class DevWebData {

	//设备id
	private long devId;
	//设备值
	private float value;
	//值字符串
	private String valueString;
	private ValueType valueType = ValueType.VALUE;

	public long getDevId() {
		return devId;
	}
	public void setDevId(long devId) {
		this.devId = devId;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public ValueType getValueType() {
		return valueType;
	}
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}
	
	public String getValueString() {
		return valueString;
	}
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	
}
