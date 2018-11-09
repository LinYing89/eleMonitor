package com.bairock.eleMonitor.data;

/**
 * 电缆温度组, 一个位置为一组, 一组有A\B\C三相电缆的温度
 * 
 * @author 44489
 *
 */
public class LineTemForm implements Comparable<LineTemForm> {

	private String place = "";
	private Device deviceA;
	private Device deviceB;
	private Device deviceC;

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		if (null != place) {
			this.place = place;
		}
	}

	public Device getDeviceA() {
		return deviceA;
	}

	public void setDeviceA(Device deviceA) {
		this.deviceA = deviceA;
	}

	public Device getDeviceB() {
		return deviceB;
	}

	public void setDeviceB(Device deviceB) {
		this.deviceB = deviceB;
	}

	public Device getDeviceC() {
		return deviceC;
	}

	public void setDeviceC(Device deviceC) {
		this.deviceC = deviceC;
	}

	@Override
	public int compareTo(LineTemForm o) {
		if (null == o) {
			return -1;
		}

		return this.place.compareTo(o.place);
	}

}
