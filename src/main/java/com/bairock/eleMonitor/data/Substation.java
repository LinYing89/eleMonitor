package com.bairock.eleMonitor.data;

import java.util.ArrayList;
import java.util.Collections;
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
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 变电所
 * 
 * @author 44489
 *
 */
@Entity
public class Substation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("station_substation")
	private Station station;

	@OneToMany(mappedBy = "substation", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("substation_msgmanager")
	private List<MsgManager> listMsgManager;

	// 设备组集合
	@OneToMany(mappedBy = "substation", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("substation_deviceGroup")
	private List<DeviceGroup> listDeviceGroup = new ArrayList<>();

	// 是否激活,选中
	@Transient
	private boolean active;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public List<MsgManager> getListMsgManager() {
		return listMsgManager;
	}

	public void setListMsgManager(List<MsgManager> listMsgManager) {
		this.listMsgManager = listMsgManager;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void addMsgManager(MsgManager msgManager) {
		msgManager.setSubstation(this);
		listMsgManager.add(msgManager);
	}

	public void removeMsgManager(MsgManager msgManager) {
		msgManager.setSubstation(null);
		listMsgManager.remove(msgManager);
	}

	public List<DeviceGroup> getListDeviceGroup() {
		return listDeviceGroup;
	}

	public void setListDeviceGroup(List<DeviceGroup> listDeviceGroup) {
		if(null != listDeviceGroup) {
			this.listDeviceGroup = listDeviceGroup;
		}
	}

	public void addDeviceGroup(DeviceGroup devGroup) {
		devGroup.setSubstation(this);
		listDeviceGroup.add(devGroup);
	}

	public void removeDeviceGroup(DeviceGroup devGroup) {
		devGroup.setSubstation(null);
		listDeviceGroup.remove(devGroup);
	}

	public MsgManager findMsgManagerByCode(int code) {
		for(MsgManager m : listMsgManager) {
			if(m.getCode() == code) {
				return m;
			}
		}
		return null;
	}
	
	/**
	 * 获取所有子节点设备
	 * 
	 * @return
	 */
	public List<Device> findDevices() {
		List<Device> listDevices = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				listDevices.addAll(collector.getListDevice());
			}
		}
		return listDevices;
	}

	/**
	 * 获取没有设备组的设备
	 * 
	 * @return
	 */
	public List<Device> findDeviceNoGroup() {
		List<Device> listDevices = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				for (Device dev : collector.getListDevice()) {
					if (dev.getDeviceGroup() == null) {
						listDevices.add(dev);
					}
				}
			}
		}
		return listDevices;
	}

	public List<Device> findCtrlDeviceNoGroup() {
		List<Device> listDevices = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				for (Device dev : collector.getListDevice()) {
					if (dev.getValueType() == ValueType.SWITCH && dev.getDeviceGroup() == null) {
						listDevices.add(dev);
					}
				}
			}
		}
		return listDevices;
	}

	public List<DeviceGroup> findCtrlDeviceGroup() {
		List<DeviceGroup> listGroup = new ArrayList<>();
		for (DeviceGroup group : listDeviceGroup) {
			if (group.getValueType() == ValueType.SWITCH && !group.isLineTem()) {
				listGroup.add(group);
			}
		}
		return listGroup;
	}

	public List<Device> findValueDeviceNoGroup() {
		List<Device> listDevices = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				for (Device dev : collector.getListDevice()) {
					if (dev.getValueType() != ValueType.SWITCH && dev.getDeviceGroup() == null) {
						listDevices.add(dev);
					}
				}
			}
		}
		return listDevices;
	}

	public List<DeviceGroup> findValueDeviceGroup() {
		List<DeviceGroup> listGroup = new ArrayList<>();
		for (DeviceGroup group : listDeviceGroup) {
			if (group.getValueType() != ValueType.SWITCH && !group.isLineTem()) {
				listGroup.add(group);
			}
		}
		return listGroup;
	}
	
	public List<DeviceGroup> findLineTemGroup() {
		List<DeviceGroup> listGroup = new ArrayList<>();
		for (DeviceGroup group : listDeviceGroup) {
			if (group.isLineTem()) {
				listGroup.add(group);
			}
		}
		return listGroup;
	}
	
	public List<DeviceEventMessage> findDeviceEventMessages(){
		List<DeviceEventMessage> list = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				for (Device dev : collector.getListDevice()) {
					list.addAll(dev.getListEventMessage());
				}
			}
		}
		Collections.sort(list);
		if(list.size() > 30) {
			return list.subList(0, 30);
		}
		return list;
	}
}
