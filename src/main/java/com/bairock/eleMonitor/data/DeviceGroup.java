package com.bairock.eleMonitor.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 设备组
 * 
 * @author 44489
 *
 */
@Entity
public class DeviceGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	private int sortIndex;
	// 组图标路径
	private String icon;

	private ValueType valueType = ValueType.VALUE;

	private boolean lineTem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("substation_deviceGroup")
	private Substation substation;

	@OneToMany(mappedBy = "deviceGroup", cascade = { CascadeType.REFRESH }, orphanRemoval = false)
	private List<Device> listDevice = new ArrayList<>();

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

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<Device> getListDevice() {
		return listDevice;
	}

	public void setListDevice(List<Device> listDevice) {
		if (null != listDevice) {
			this.listDevice = listDevice;
			sortDevices();
		}
	}

	public Substation getSubstation() {
		return substation;
	}

	public void setSubstation(Substation substation) {
		this.substation = substation;
	}

	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public boolean isLineTem() {
		return lineTem;
	}

	public void setLineTem(boolean lineTem) {
		this.lineTem = lineTem;
	}

	public Device findDeviceById(long id) {
		for (Device device : listDevice) {
			if (device.getId() == id) {
				return device;
			}
		}
		return null;
	}

	public boolean addDevice(Device device) {
		if (null != device && !listDevice.contains(device)) {
			device.setDeviceGroup(this);
			listDevice.add(device);
			device.setSortIndexInGroup(listDevice.size());
			return true;
		}
		return false;
	}

	public void removeDevice(Device device) {
		if (null == device) {
			return;
		}
		device.setDeviceGroup(null);
		listDevice.remove(device);
	}

	public void sortDevices() {
		Collections.sort(listDevice, Comparator.comparing(Device::getSortIndexInGroup));
	}
	
	@Transient
	@JsonIgnore
	public boolean isAlarming() {
		for(Device dev : listDevice) {
			if(dev.isAlarming()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 将设备的排序上移
	 * 
	 * @param dev
	 * @return 如果已经时第一个无法上移, 返回null 否则返回被重新排序的两个设备的数组, 数组顺序按更新后的排序顺序
	 */
	public Device[] moveUpDevice(Device dev) {
		int index = listDevice.indexOf(dev);
		if (index == 0) {
			return null;
		}
		Device devTarget = listDevice.get(index - 1);
		if(dev.getSortIndexInGroup() == devTarget.getSortIndexInGroup()) {
			dev.setSortIndexInGroup(dev.getSortIndexInGroup() + 1);
		}
		changeSortIndex(dev, devTarget);
		sortDevices();
		return new Device[] { dev, devTarget };
	}

	/**
	 * 将设备的排序上移
	 * 
	 * @param dev
	 * @return 如果已经时第一个无法上移, 返回null 否则返回被重新排序的两个设备的数组, 数组顺序按更新后的排序顺序
	 */
	public Device[] moveDownDevice(Device dev) {
		int index = listDevice.indexOf(dev);
		if (index >= listDevice.size() - 1) {
			return null;
		}
		Device devTarget = listDevice.get(index + 1);
		if(dev.getSortIndexInGroup() == devTarget.getSortIndexInGroup()) {
			devTarget.setSortIndexInGroup(devTarget.getSortIndexInGroup() + 1);
		}
		changeSortIndex(dev, devTarget);
		sortDevices();
		return new Device[] { devTarget, dev };
	}

	private void changeSortIndex(Device src, Device target) {
		int targetSortIndex = target.getSortIndexInGroup();
		target.setSortIndexInGroup(src.getSortIndexInGroup());
		src.setSortIndexInGroup(targetSortIndex);
	}
}
