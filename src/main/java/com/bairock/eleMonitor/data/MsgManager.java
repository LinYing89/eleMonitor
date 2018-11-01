package com.bairock.eleMonitor.data;

import java.util.Arrays;
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
 * 
 * @author 44489
 *
 */
@Entity
public class MsgManager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int code;

	private String name;
	// 安装位置
	private String place;

	@ManyToOne
	@JsonBackReference("substation_msgmanager")
	private Substation substation;

	@OneToMany(mappedBy = "msgManager", cascade = CascadeType.ALL, orphanRemoval = true)
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

	public void addCollector(Collector collector) {
		collector.setMsgManager(this);
		listCollector.add(collector);
	}

	public void removeCollector(Collector collector) {
		collector.setMsgManager(null);
		listCollector.remove(collector);
	}

	public Collector findCollectorByCode(int code) {
		for (Collector c : listCollector) {
			if (c.getCode() == code) {
				return c;
			}
		}
		return null;
	}

	public void handler(byte[] by) {

		// 去掉校验码
		byte[] byAllData = Arrays.copyOfRange(by, 0, by.length - 2);

		int index = 0;

		while (index < byAllData.length) {
			// 总线号,索引0,长度1
			index += 1;
			// 采集终端号,索引1,长度1
			byte collectorCode = byAllData[index];
			index += 1;
			// 起始编号(地址),索引2,长度2
			//int beginAddress = byAllData[index] << 8 | byAllData[index + 1];
			index += 2;
			// 数据长度,索引4,长度2
			int dataLen = byAllData[index] << 8 | byAllData[index + 1];
			index += 2;
			// 数据,索引6, 长度dataLen
			byte[] byteData = Arrays.copyOfRange(byAllData, index, index + dataLen);
			index += dataLen;

			Collector collector = findCollectorByCode(collectorCode);
			if (null != collector) {
				collector.handler(byteData);
			}
		}
	}
}
