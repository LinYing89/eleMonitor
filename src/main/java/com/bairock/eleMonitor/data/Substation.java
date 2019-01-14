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

import com.bairock.eleMonitor.enums.StationState;
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
	
	@Transient
	private StationState substationState = StationState.OFFLINE;
	
	@OneToMany(mappedBy = "substation", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("substation_msgmanager")
	private List<MsgManager> listMsgManager = new ArrayList<>();

	// 设备组集合
	@OneToMany(mappedBy = "substation", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("substation_deviceGroup")
	private List<DeviceGroup> listDeviceGroup = new ArrayList<>();

	@OneToMany(mappedBy = "substation", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonManagedReference("substation_doorCard")
	private List<DoorCard> listDoorCard = new ArrayList<>();

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
		if (null != listDeviceGroup) {
			this.listDeviceGroup = listDeviceGroup;
		}
	}

	public List<DoorCard> getListDoorCard() {
		return listDoorCard;
	}

	public void setListDoorCard(List<DoorCard> listDoorCard) {
		this.listDoorCard = listDoorCard;
	}

	public void addDeviceGroup(DeviceGroup devGroup) {
		devGroup.setSubstation(this);
		listDeviceGroup.add(devGroup);
	}

	public void removeDeviceGroup(DeviceGroup devGroup) {
		devGroup.setSubstation(null);
		listDeviceGroup.remove(devGroup);
	}

	public void addDoorCard(DoorCard doorCard) {
		doorCard.setSubstation(this);
		listDoorCard.add(doorCard);
	}

	public void removeDoorCard(DoorCard doorCard) {
		doorCard.setSubstation(null);
		listDoorCard.remove(doorCard);
	}

	public MsgManager findMsgManagerByCode(int code) {
		for (MsgManager m : listMsgManager) {
			if (m.getCode() == code) {
				return m;
			}
		}
		return null;
	}

	public StationState getSubstationState() {
		return substationState;
	}

	public void setSubstationState(StationState substationState) {
		this.substationState = substationState;
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

	public List<Device> findAllCtrlDevice() {
		List<Device> listDevices = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				for (Device dev : collector.getListDevice()) {
					if (dev.getValueType() == ValueType.SWITCH) {
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

	public List<Device> findAllValueDevice() {
		List<Device> listDevices = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				for (Device dev : collector.getListDevice()) {
					if (dev.getValueType() != ValueType.SWITCH) {
						listDevices.add(dev);
					}
				}
			}
		}
		return listDevices;
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

	/**
	 * 获取所有门控设备
	 * 
	 * @return
	 */
	public List<Device> findDoorDevice() {
		List<Device> list = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				for (Device dev : collector.getListDevice()) {
					if (dev.getDeviceCategory() == DeviceCategory.DOOR) {
						list.add(dev);
					}
				}
			}
		}
		return list;
	}

	public List<DeviceEventMessage> findDeviceEventMessages() {
		List<DeviceEventMessage> list = new ArrayList<>();
		for (MsgManager manager : listMsgManager) {
			for (Collector collector : manager.getListCollector()) {
				for (Device dev : collector.getListDevice()) {
					list.addAll(dev.getListEventMessage());
				}
			}
		}
		Collections.sort(list);
		if (list.size() > 30) {
			return list.subList(0, 30);
		}
		return list;
	}
	
	/**
	 * 获取报警的设备
	 * @return
	 */
	public List<Device> findAlarmDevice(){
		List<Device> list = new ArrayList<>();
		for (MsgManager mm : getListMsgManager()) {
			if(mm.getMsgManagerState() == MsgManagerState.OFFLINE) {
				continue;
			}
			for (Collector c : mm.getListCollector()) {
				for (Device d : c.getListDevice()) {
					if (d.isAlarming()) {
						list.add(d);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取离线的通信机
	 * @return
	 */
	public List<MsgManager> findOfflineMsgManager(){
		List<MsgManager> list = new ArrayList<>();
		for (MsgManager mm : getListMsgManager()) {
			if(mm.getMsgManagerState() == MsgManagerState.OFFLINE) {
				list.add(mm);
			}
		}
		return list;
	}

	public StationState refreshState() {
		//先判断是否有报警
		for (MsgManager mm : getListMsgManager()) {
			if(mm.getMsgManagerState() == MsgManagerState.OFFLINE) {
				continue;
			}
			for (Collector c : mm.getListCollector()) {
				for (Device d : c.getListDevice()) {
					if (d.isAlarming()) {
						setSubstationState(StationState.ALARM);
						//将station设置为报警
						getStation().setState(StationState.ALARM);
						return StationState.ALARM;
					}
				}
			}
		}
		//再判断是否有离线
		for (MsgManager mm : getListMsgManager()) {
			if(mm.getMsgManagerState() == MsgManagerState.OFFLINE) {
				setSubstationState(StationState.OFFLINE);
				//将station设置为离线
				getStation().setState(StationState.OFFLINE);
				return StationState.OFFLINE;
			}
		}
		//否则为正常
		setSubstationState(StationState.NORMAL);
		return StationState.NORMAL;
	}
	
	/**
	 * 获取报警或离线的通信机的个数
	 * @return
	 */
	public int countUnsccessMsgManager() {
		int count = 0;
		for (MsgManager mm : listMsgManager) {
			if (mm.getMsgManagerState() != MsgManagerState.SUCCESS) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 获取所有通信机状态统计
	 * 
	 * @return 长度2, 0:正常个数, 1:离线个数
	 */
	public int[] findMsgManagerStateCount() {
		int[] stateCount = new int[2];
		for (MsgManager mm : listMsgManager) {
			if (mm.getMsgManagerState() == MsgManagerState.SUCCESS) {
				stateCount[0]++;
			} else {
				stateCount[1]++;
			}
		}
		return stateCount;
	}
}
