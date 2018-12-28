package com.bairock.eleMonitor.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 门卡
 * 限时授权,  几点到几点
 * @author 44489
 *
 */
@Entity
public class DoorCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	//使用者名称
	private String username;
	//卡号
	private String cardNum;
	
	//时限开始时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date limitTimeStart = new Date();
	//时限结束时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date limitTimeEnd = new Date();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("substation_doorCard")
	private Substation substation;
	
	@OneToMany(mappedBy="doorCard", cascade=CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("doorCard_doorAuthority")
	private List<DoorAuthority> listDoorAuthority = new ArrayList<>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public Date getLimitTimeStart() {
		return limitTimeStart;
	}
	public void setLimitTimeStart(Date limitTimeStart) {
		this.limitTimeStart = limitTimeStart;
	}
	public Date getLimitTimeEnd() {
		return limitTimeEnd;
	}
	public void setLimitTimeEnd(Date limitTimeEnd) {
		this.limitTimeEnd = limitTimeEnd;
	}
	public Substation getSubstation() {
		return substation;
	}
	public void setSubstation(Substation substation) {
		this.substation = substation;
	}
	public List<DoorAuthority> getListDoorAuthority() {
		return listDoorAuthority;
	}
	public void setListDoorAuthority(List<DoorAuthority> listDoorAuthority) {
		this.listDoorAuthority = listDoorAuthority;
	}
	
	public void addDoorAuthority(DoorAuthority doorAuthority) {
		doorAuthority.setDoorCard(this);
		listDoorAuthority.add(doorAuthority);
	}
	
	public void removeDoorAuthority(DoorAuthority doorAuthority) {
		doorAuthority.setDoorCard(null);
		listDoorAuthority.remove(doorAuthority);
	}
	
	@Transient
	@JsonIgnore
	public String getLimitTimeStartStr() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(null == limitTimeStart) {
			limitTimeStart = new Date();
		}
		return format.format(limitTimeStart);
	}
	
	@Transient
	@JsonIgnore
	public String getLimitTimeEndStr() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(null == limitTimeEnd) {
			limitTimeEnd = new Date();
		}
		return format.format(limitTimeEnd);
	}
	
	@Transient
	@JsonIgnore
	public String getLimitTimeStr() {
		return getLimitTimeStartStr() + " ~ " + getLimitTimeEndStr();
	}
}
