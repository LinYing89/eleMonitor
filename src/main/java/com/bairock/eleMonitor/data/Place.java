package com.bairock.eleMonitor.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * 位置信息
 * @author 44489
 *
 */
@Entity
public class Place {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private int sortIndex;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("substation_plcae")
	private Substation substation;
	
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
	public Substation getSubstation() {
		return substation;
	}
	public void setSubstation(Substation substation) {
		this.substation = substation;
	}
	
}
