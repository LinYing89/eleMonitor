package com.bairock.eleMonitor.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * 设备组
 * @author 44489
 *
 */
@Entity
public class DeviceGroup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	private int sortIndex;
	//组图标路径
	private String icon;
	
	@ManyToOne
	@JsonBackReference("substation_deviceGroup")
	private Substation substation;
	
	@OneToMany(orphanRemoval=false)
	private List<Device> listDevice;
	
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
		this.listDevice = listDevice;
	}
	public Substation getSubstation() {
		return substation;
	}
	public void setSubstation(Substation substation) {
		this.substation = substation;
	}
	
	public Device findDeviceById(long id) {
		for(Device device : listDevice) {
			if(device.getId() == id) {
				return device;
			}
		}
		return null;
	}
	
	public void addDevice(Device device) {
		device.setDeviceGroup(this);
		listDevice.add(device);
	}
	
	public void removeDevice(Device device) {
		if(null == device) {
			return;
		}
		device.setDeviceGroup(null);
		listDevice.remove(device);
	}
}