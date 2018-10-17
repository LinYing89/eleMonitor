package com.bairock.eleMonitor.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * 设备
 * @author 44489
 *
 */
@Entity
public class Device {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	//起始地址
	private int beginAddress;
	//数据长度
	private int dataLength;
	//字节顺序
	private int byteOrder;
	//值类型,整数, 浮点数
	private ValueType valueType;
	//值格式
	private ValueFormat valeFormat;
	//系数
	private float coefficient;
	//单位
	private String unit;
	
	@ManyToOne
	@JsonBackReference("collector_device")
	private Collector collector;
	
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
	public int getBeginAddress() {
		return beginAddress;
	}
	public void setBeginAddress(int beginAddress) {
		this.beginAddress = beginAddress;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public int getByteOrder() {
		return byteOrder;
	}
	public void setByteOrder(int byteOrder) {
		this.byteOrder = byteOrder;
	}
	public ValueType getValueType() {
		return valueType;
	}
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}
	public ValueFormat getValeFormat() {
		return valeFormat;
	}
	public void setValeFormat(ValueFormat valeFormat) {
		this.valeFormat = valeFormat;
	}
	public float getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Collector getCollector() {
		return collector;
	}
	public void setCollector(Collector collector) {
		this.collector = collector;
	}
	
}
