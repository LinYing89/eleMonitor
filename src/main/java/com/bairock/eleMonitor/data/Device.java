package com.bairock.eleMonitor.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

	@OneToOne
	@JoinColumn(name = "place_id")
	private Place place;

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

	private DeviceCategory deviceCategory = DeviceCategory.DEFAULT;

	@ManyToOne
	@JsonIgnore
	private DeviceGroup deviceGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("collector_device")
	private Collector collector;

	@OneToMany(mappedBy = "device", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
//	@JsonManagedReference("device_event")
	@JsonIgnore
	private List<DeviceEventMessage> listEventMessage = new ArrayList<>();

	@OneToMany(mappedBy = "device", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonManagedReference("device_linkage")
	private List<Linkage> listLinkage = new ArrayList<>();

	@Transient
	@JsonIgnore
	private List<Effect> listTriggedEffect = new ArrayList<>();
	/**
	 * 报警中
	 */
	@Transient
	private boolean alarming;

	@Transient
	@JsonIgnore
	private OnValueListener onValueListener;
	@Transient
	@JsonIgnore
	private OnLinkageTriggeredListener onLinkageTriggeredListener;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Place getPlace() {
		if (null == place) {
			place = new Place();
			place.setName("无");
		}
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
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

	public DeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(DeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
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
		boolean changed = false;
		if (Math.abs(value - this.value) >= 0.01) {
			this.value = value;
			changed = true;
		}
		// 分析连锁, 先连锁, 连锁可能导致报警
		// 不论值有没有改变, 都要检查连锁, 防止有漏掉的没有触发的连锁
		analysisLinkage();
		if (changed) {
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

	public OnLinkageTriggeredListener getOnLinkageTriggeredListener() {
		return onLinkageTriggeredListener;
	}

	public void setOnLinkageTriggeredListener(OnLinkageTriggeredListener onLinkageTriggeredListener) {
		this.onLinkageTriggeredListener = onLinkageTriggeredListener;
	}

	public List<Linkage> getListLinkage() {
		return listLinkage;
	}

	public void setListLinkage(List<Linkage> listLinkage) {
		this.listLinkage = listLinkage;
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

	public List<Effect> getListTriggedEffect() {
		return listTriggedEffect;
	}

	public void clearListTriggedEffect() {
		listTriggedEffect.clear();
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

	private void analysisLinkage() {
		clearListTriggedEffect();
		// List<Effect> listEffect = new ArrayList<>();
		boolean alarm = false;
		for (Linkage linkage : listLinkage) {
			if (linkage.compareResult()) {
				if (linkage.isAlarming() && !alarm) {
					alarm = true;
				}
				listTriggedEffect.addAll(linkage.getListEffect());
			}
		}
		setAlarming(alarm);
//		if(!listEffect.isEmpty()) {
//			if(null != onLinkageTriggeredListener) {
//				onLinkageTriggeredListener.onLinkageTriggered(this, listEffect);
//			}
//		}
	}

	public void addLinkage(Linkage linkage) {
		if (null != linkage && !listLinkage.contains(linkage)) {
			linkage.setDevice(this);
			listLinkage.add(linkage);
		}
	}

	public void removeLinkage(Linkage linkage) {
		listLinkage.remove(linkage);
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

	public interface OnLinkageTriggeredListener {
		/**
		 * 设备值改变
		 * 
		 * @param device
		 * @param value
		 */
		void onLinkageTriggered(Device device, List<Effect> listEffect);
	}

	@Override
	public int compareTo(Device o) {
		if (o == null) {
			return -1;
		}
		return this.sortIndex - o.sortIndex;
	}

}
