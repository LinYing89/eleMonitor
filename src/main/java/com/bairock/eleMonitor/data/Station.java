package com.bairock.eleMonitor.data;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 站房站点
 * @author 44489
 *
 */
@Entity
public class Station {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String name;
	private String address;
	private double lat;
	private double lng;
	private String tel;
	private String remark;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerTime;
	
	@OneToMany(mappedBy="station", cascade=CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("station_substation")
	private List<Substation> listSubstation;
	
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public List<Substation> getListSubstation() {
		return listSubstation;
	}
	public void setListSubstation(List<Substation> listSubstation) {
		this.listSubstation = listSubstation;
	}
	
	public Substation addSubstation(Substation substation) {
		if(null == substation) {
			return null;
		}
		substation.setStation(this);
		listSubstation.add(substation);
		return substation;
	}
	
	public Substation removeSubstation(Substation substation) {
		if(null == substation) {
			return null;
		}
		listSubstation.remove(substation);
		substation.setStation(null);
		return substation;
	}
}
