package com.bairock.eleMonitor.data.cable;

import com.bairock.eleMonitor.Util;

/**
 * 相
 * 
 * @author 44489
 *
 */
public class Phase {

	// 电压
	private float voltage;
	// 电压数据Id, 在网页中标识, 方便更新
	private long voltageId;
	// 电流
	private float current;
	// 电流数据Id, 在网页中标识, 方便更新
	private long currentId;
	// 功率因数
	private float factor;
	// 因数数据Id, 在网页中标识, 方便更新
	private long factorId;
	//线缆温度
	private float tem;
	// 温度ID
	private long temId;

	public float getVoltage() {
		return voltage;
	}

	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}

	public float getCurrent() {
		return current;
	}

	public void setCurrent(float current) {
		this.current = current;
	}

	public float getFactor() {
		return factor;
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}

	public long getVoltageId() {
		return voltageId;
	}

	public void setVoltageId(long voltageId) {
		this.voltageId = voltageId;
	}

	public long getCurrentId() {
		return currentId;
	}

	public void setCurrentId(long currentId) {
		this.currentId = currentId;
	}

	public long getFactorId() {
		return factorId;
	}

	public void setFactorId(long factorId) {
		this.factorId = factorId;
	}

	public float getTem() {
		return tem;
	}

	public void setTem(float tem) {
		this.tem = tem;
	}

	public long getTemId() {
		return temId;
	}

	public void setTemId(long temId) {
		this.temId = temId;
	}

	public float getPower() {
		float power = voltage * current * factor / 1000;
		return Util.scale(power);
	}
}
