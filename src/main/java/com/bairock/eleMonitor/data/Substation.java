package com.bairock.eleMonitor.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
 * @author 44489
 *
 */
@Entity
public class Substation {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String name;
	private String remark;
	
	@ManyToOne
	@JsonBackReference("station_substation")
	private Station station;
	
	@OneToMany(mappedBy="substation", cascade=CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("substation_msgmanager")
	private List<MsgManager> listMsgManager;
	
	//是否激活,选中
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
	
	
}
