package com.bairock.eleMonitor.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * 设备
 * @author 44489
 *
 */

@Entity
public class Device {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	//通信管理机号
	private int managerCode;
	//总线号
	private int busCode;
	//采集终端号
	private int collectorCode;
	//起始地址
	private int beginAddress;
	//数据长度
	private int dataLength;
	//值类型
	private DeviceValueType valueType;
	
	@ManyToOne
	@JsonBackReference("station_device")
	private Station station;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getManagerCode() {
		return managerCode;
	}
	public void setManagerCode(int managerCode) {
		this.managerCode = managerCode;
	}
	public int getBusCode() {
		return busCode;
	}
	public void setBusCode(int busCode) {
		this.busCode = busCode;
	}
	public int getCollectorCode() {
		return collectorCode;
	}
	public void setCollectorCode(int collectorCode) {
		this.collectorCode = collectorCode;
	}
	public int getBeginAddress() {
		return beginAddress;
	}
	public void setBeginAddress(int beginAddress) {
		this.beginAddress = beginAddress;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public DeviceValueType getValueType() {
		return valueType;
	}
	public void setValueType(DeviceValueType valueType) {
		this.valueType = valueType;
	}
	
	
}
