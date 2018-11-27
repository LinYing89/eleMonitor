package com.bairock.eleMonitor.data;

import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 连锁
 * 
 * @author 44489
 *
 */
@Entity
public class Linkage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("device_linkage")
	private Device device;
	// 比较符号, 1==, 0>, 2<
	private int compareSymbol;
	// 比较值
	private float compareValue;
	private boolean alarming;

	@OneToMany(mappedBy = "linkage", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonManagedReference("linkage_effect")
	private List<Effect> listEffect = new ArrayList<Effect>();

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

	public int getCompareSymbol() {
		return compareSymbol;
	}

	public void setCompareSymbol(int compareSymbol) {
		this.compareSymbol = compareSymbol;
	}

	public float getCompareValue() {
		return compareValue;
	}

	public void setCompareValue(float compareValue) {
		this.compareValue = compareValue;
	}

	public boolean isAlarming() {
		return alarming;
	}

	public void setAlarming(boolean alarming) {
		this.alarming = alarming;
	}

	public List<Effect> getListEffect() {
		return listEffect;
	}

	public void setListEffect(List<Effect> listEffect) {
		this.listEffect = listEffect;
	}
	
	@Transient
	@JsonIgnore
	public String getStrValue() {
		String symbol = "";
		switch(compareSymbol) {
		case 0:
			symbol = ">";
			break;
		case 1:
			symbol = "=";
			break;
		case 2:
			symbol = "<";
			break;
		}
		return symbol + "  " + compareValue;
	}

	public void addEffect(Effect effect) {
		if (null != effect && !listEffect.contains(effect)) {
			effect.setLinkage(this);
			listEffect.add(effect);
		}
	}

	public void removeEffect(Effect effect) {
		listEffect.remove(effect);
	}

	public boolean compareResult() {
		switch (compareSymbol) {
		case 0:
			//大于
			if(device.getValue() > compareValue) {
				return true;
			}
			break;
		case 1:
			//等于
			if(device.getValue() == compareValue) {
				return true;
			}
			break;
		case 2:
			//小于
			if(device.getValue() < compareValue) {
				return true;
			}
			break;
		}
		return false;
	}
}
