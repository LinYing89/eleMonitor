package com.bairock.eleMonitor.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	// 功能码
	private int functionCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("msgmanager_collector")
	private MsgManager msgManager;

	@OneToMany(mappedBy = "collector", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("collector_device")
	private List<Device> listDevice = new ArrayList<>();

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
		if (null != listDevice) {
			this.listDevice = listDevice;
		}
	}

	public void addDevice(Device device) {
		device.setCollector(this);
		listDevice.add(device);
	}

	public void removeDevice(Device device) {
//		device.setCollector(null);
		listDevice.remove(device);
	}

	public Device findDeviceById(long id) {
		for (Device d : listDevice) {
			if (d.getId() == id) {
				return d;
			}
		}
		return null;
	}

	public void handler(byte[] byData) {
		// 数据类型,1,2开关量, 3,4数值量

		switch (functionCode) {
		case 1:
		case 2:
			// 输出和输入开关量, 开关, 报警等, 按位计算
			for (Device device : listDevice) {
				if (device.getValueType() == ValueType.SWITCH || device.getValueType() == ValueType.ALARM) {
					// 第几位
					int bit = device.getBeginAddress();
					// 位所在的字节, 位的表示方法为所有字节先高后低
					int whichByte = byData.length - 1 - (bit / 8);
					if (whichByte < 0 || whichByte >= byData.length) {
						continue;
					}
					byte b1 = byData[whichByte];
					int value = b1 >> bit & 1;
					device.setValue(value);
				}
			}
			break;
		case 3:
		case 4:
			// 数值量, 温度等, 按字节计算, 配置的数据长度位寄存器长度, 一个寄存器2个字节
			// 返回的数据为字节的长度, 返回的长度应为dataLength*2
			for (Device device : listDevice) {
				if (device.getValueType() == ValueType.VALUE) {
					// 地址为寄存器的地址, *2为字节的起始地址
					int from = (device.getBeginAddress() - this.beginAddress) * 2;
					if (from >= 0 && from < byData.length) {
						// 实际截取的字节数应为dataLength*2. 因为dataLength为字数, 即寄存器的长度
						int to = from + (device.getDataLength() * 2);
						if (to <= byData.length) {
							byte[] byDevData = Arrays.copyOfRange(byData, from, to);
							device.handler(byDevData);
						}
					}
				}
			}
			break;
		}

	}

}
