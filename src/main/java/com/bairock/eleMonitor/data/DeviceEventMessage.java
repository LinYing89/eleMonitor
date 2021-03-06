package com.bairock.eleMonitor.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 设备事件信息
 * @author 44489
 *
 */
@Entity
public class DeviceEventMessage implements Comparable<DeviceEventMessage>{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
//	@JsonBackReference("device_event")
	@JsonIgnore
	private Device device;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventTime;
	
	private String message;
	
	@Transient
	@JsonIgnore
	private boolean alarm;

	@Transient
	@JsonIgnore
	private String timeFormat;
	
	@Transient
	@JsonIgnore
	private SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
	
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

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isAlarm() {
		return alarm;
	}

	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
	}

	public String getTimeFormat() {
		if(null == timeFormat || timeFormat.isEmpty()) {
			this.timeFormat = SimpleDateFormat.format(eventTime);
		}
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	@Override
	public int compareTo(DeviceEventMessage o) {
		if (o == null) {
			return -1;
		}
		return o.eventTime.compareTo(this.eventTime);
	}
	
}
