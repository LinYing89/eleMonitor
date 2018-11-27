package com.bairock.eleMonitor.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 连锁目标, 受影响者
 * @author 44489
 *
 */
@Entity
public class Effect {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	//目标设备
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "dev_id")
	private Device device;
	//连锁后期望的目标值
	private float value;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference("linkage_effect")
	private Linkage linkage;
	
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
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public Linkage getLinkage() {
		return linkage;
	}
	public void setLinkage(Linkage linkage) {
		this.linkage = linkage;
	}
	
	@Transient
	@JsonIgnore
	public String getStrValue() {
		if(value == 0) {
			return "关";
		}else {
			return "开";
		}
	}
	
}
