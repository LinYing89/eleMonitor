package com.bairock.eleMonitor.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 采集终端
 * 
 * @author 44489
 *
 */
@Entity
public class Collector {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int code;
	private String name;

	// 总线号
	private int busCode;
	// 起始地址
	private int beginAddress;
	// 数据长度
	private int dataLength;
	// 值类型
	private DataType dataType;
	//功能码
	private int functionCode;

	@ManyToOne
	@JsonBackReference("msgmanager_collector")
	private MsgManager msgManager;
	
	@OneToMany(mappedBy="collector", cascade=CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("collector_device")
	private List<Device> listDevice;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBusCode() {
		return busCode;
	}

	public void setBusCode(int busCode) {
		this.busCode = busCode;
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

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public int getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
	}

	public MsgManager getMsgManager() {
		return msgManager;
	}

	public void setMsgManager(MsgManager msgManager) {
		this.msgManager = msgManager;
	}

	public List<Device> getListDevice() {
		return listDevice;
	}

	public void setListDevice(List<Device> listDevice) {
		this.listDevice = listDevice;
	}
	
	public void addDevice(Device device) {
		device.setCollector(this);
		listDevice.add(device);
	}
	
	public void removeDevice(Device device) {
		device.setCollector(null);
		listDevice.remove(device);
	}
	
}
