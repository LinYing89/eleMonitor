package com.bairock.eleMonitor.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 通信管理机
 * @author 44489
 *
 */
@Entity
public class MsgManager {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private int code;
	
	private String name;
	//安装位置
	private String place;
	
	@ManyToOne
	@JsonBackReference("substation_msgmanager")
	private Substation substation;
	
	@OneToMany(mappedBy="msgManager", cascade=CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("msgmanager_collector")
	private List<Collector> listCollector;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public Substation getSubstation() {
		return substation;
	}
	public void setSubstation(Substation substation) {
		this.substation = substation;
	}
	public List<Collector> getListCollector() {
		return listCollector;
	}
	public void setListCollector(List<Collector> listCollector) {
		this.listCollector = listCollector;
	}
	
}
