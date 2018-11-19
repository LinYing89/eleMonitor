package com.bairock.eleMonitor.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.bairock.eleMonitor.Util;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 设备
 * 
 * @author 44489
 *
 */
@Entity
public class Device implements Comparable<Device> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String place = "";

	private String name;

	// 起始地址
	private int beginAddress;
	// 数据长度
	private int dataLength;
	// 字节顺序
	private int byteOrder;
	// 值类型
	private ValueType valueType = ValueType.VALUE;
	// 报警设备报警时返回的值, valueType为ALARM时有效, 有的设备报警值为0, 有的设备报警值为1
	private int alarmTriggerValue = 1;
	// 值格式
	private ValueFormat valueFormat = ValueFormat.ABS;
	// 系数
	private float coefficient = 1;
	// 单位
	private String unit = "";
	// 排序索引
	private int sortIndex;
	// 在设备组中的排序索引
	private int sortIndexInGroup;
	// 组图标路径
	private String icon = "";

	private String remark = "";

	private float value;

	@ManyToOne
	@JsonIgnore
	private DeviceGroup deviceGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("collector_device")
	private Collector collector;

	@OneToMany(mappedBy = "device", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonManagedReference("device_event")
	private List<DeviceEventMessage> listEventMessage = new ArrayList<>();

	@OneToMany(mappedBy = "device", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonManagedReference("device_history")
	private List<DeviceValueHistory> listValueHistory = new ArrayList<>();

	/**
	 * 报警中
	 */
	@Transient
	private boolean alarming;

	@Transient
	private OnValueListener onValueListener;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		if (null != place) {
			this.place = place;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(int byteOrder) {
		this.byteOrder = byteOrder;
	}

	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public int getAlarmTriggerValue() {
		return alarmTriggerValue;
	}

	public void setAlarmTriggerValue(int alarmTriggerValue) {
		this.alarmTriggerValue = alarmTriggerValue;
	}

	public ValueFormat getValueFormat() {
		return valueFormat;
	}

	public void setValueFormat(ValueFormat valueFormat) {
		this.valueFormat = valueFormat;
	}

	public float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Collector getCollector() {
		return collector;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public int getSortIndexInGroup() {
		return sortIndexInGroup;
	}

	public void setSortIndexInGroup(int sortIndexInGroup) {
		this.sortIndexInGroup = sortIndexInGroup;
	}

	public DeviceGroup getDeviceGroup() {
		return deviceGroup;
	}

	public void setDeviceGroup(DeviceGroup deviceGroup) {
		this.deviceGroup = deviceGroup;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		// 保留两位小数
		value = Util.scale(value);
		if (null != onValueListener) {
			onValueListener.onValueReceived(this, value);
		}
		if (Math.abs(value - this.value) >= 0.01) {
			this.value = value;

			if (null != onValueListener) {
				onValueListener.onValueChanged(this, value);
			}
		}
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<DeviceEventMessage> getListEventMessage() {
		return listEventMessage;
	}

	public void setListEventMessage(List<DeviceEventMessage> listEventMessage) {
		if (null != listEventMessage) {
			this.listEventMessage = listEventMessage;
		}
	}

	public List<DeviceValueHistory> getListValueHistory() {
		return listValueHistory;
	}

	public void setListValueHistory(List<DeviceValueHistory> listValueHistory) {
		if (null != listValueHistory) {
			this.listValueHistory = listValueHistory;
		}
	}

	public boolean isAlarming() {
		if (valueType == ValueType.ALARM) {
			alarming = (value == alarmTriggerValue);
		}
		return alarming;
	}

	public void setAlarming(boolean alarming) {
		this.alarming = alarming;
	}

	@Transient
	@JsonIgnore
	public String getValueString() {
		String valueString = "";
		switch (valueType) {
		case ALARM:
			if (value != alarmTriggerValue) {
				valueString = "正常";
			} else {
				valueString = "异常";
			}
			break;
		case SWITCH:
			if (value == 0) {
				valueString = "关";
			} else {
				valueString = "开";
			}
			break;
		default:
			valueString = String.valueOf(value);
			break;
		}
		return valueString;
	}

	public void addEventMessage(DeviceEventMessage event) {
		if (null != event && !listEventMessage.contains(event)) {
			event.setDevice(this);
			listEventMessage.add(event);
		}
	}

	public void removeEventMessage(DeviceEventMessage event) {
		listEventMessage.remove(event);
	}

	public void addValueHistory(DeviceValueHistory history) {
		if (null != history && !listValueHistory.contains(history)) {
			history.setDevice(this);
			listValueHistory.add(history);
		}
	}

	public void removeValueHistoru(DeviceValueHistory history) {
		listValueHistory.remove(history);
	}

	public void handler(byte[] byData) {
		float value = 0;
		if (byteOrder == 12) {
			value = bytesToInt12(byData) * coefficient;
		} else {
			value = bytesToInt21(byData) * coefficient;
		}
		setValue(value);
	}

	private int bytesToInt12(byte[] by) {
		int value = 0;
		for (int i = 0; i < by.length; i++) {
			value = value << 8 | (by[i] & 0xff);
		}
		return value;
	}

	private int bytesToInt21(byte[] by) {
		int value = 0;
		for (int i = by.length - 1; i >= 0; i--) {
			value = value << 8 | (by[i] & 0xff);
		}
		return value;
	}

	public void setOnValueListener(OnValueListener onValueListener) {
		this.onValueListener = onValueListener;
	}

	public OnValueListener getOnValueListener() {
		return onValueListener;
	}

	public interface OnValueListener {
		/**
		 * 设备值改变
		 * 
		 * @param device
		 * @param value
		 */
		void onValueChanged(Device device, float value);

		/**
		 * 收到设备值, 不一定是改变了
		 * 
		 * @param device
		 * @param value
		 */
		void onValueReceived(Device device, float value);
	}

	@Override
	public int compareTo(Device o) {
		if (o == null) {
			return -1;
		}
		return this.sortIndex - o.sortIndex;
	}

}
