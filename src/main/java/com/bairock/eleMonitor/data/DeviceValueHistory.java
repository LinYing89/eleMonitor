package com.bairock.eleMonitor.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * 设备历史纪录
 * @author 44489
 *
 */
@Entity
public class DeviceValueHistory implements Comparable<DeviceValueHistory>{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JsonBackReference("device_history")
	private Device device;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	private float value;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public int compareTo(DeviceValueHistory o) {
		if (o == null) {
			return -1;
		}
		return this.time.compareTo(o.time);
	}
}
