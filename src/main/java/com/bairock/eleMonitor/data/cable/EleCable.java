package com.bairock.eleMonitor.data.cable;

/**
 * 电力线缆
 * @author 44489
 *
 */
public class EleCable {

	//线缆所在的组名
	private String groupName;
	//A相
	private Phase phaseA;
	//B相
	private Phase phaseB;
	//C相
	private Phase phaseC;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Phase getPhaseA() {
		return phaseA;
	}
	public void setPhaseA(Phase phaseA) {
		this.phaseA = phaseA;
	}
	public Phase getPhaseB() {
		return phaseB;
	}
	public void setPhaseB(Phase phaseB) {
		this.phaseB = phaseB;
	}
	public Phase getPhaseC() {
		return phaseC;
	}
	public void setPhaseC(Phase phaseC) {
		this.phaseC = phaseC;
	}
}
