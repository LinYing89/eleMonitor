package com.bairock.eleMonitor.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 门卡刷卡历史纪录
 * 限时授权,  几点到几点
 * @author 44489
 *
 */
@Entity
public class DoorCardHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String cardNum;
	private String cardUsername;
	private String state;
	private Date noteTime;
	private long substationId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCardUsername() {
		return cardUsername;
	}
	public void setCardUsername(String cardUsername) {
		this.cardUsername = cardUsername;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getNoteTime() {
		return noteTime;
	}
	public void setNoteTime(Date noteTime) {
		this.noteTime = noteTime;
	}
	public long getSubstationId() {
		return substationId;
	}
	public void setSubstationId(long substationId) {
		this.substationId = substationId;
	}

}
