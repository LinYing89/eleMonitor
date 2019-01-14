package com.bairock.eleMonitor.data;

/**
 * 通信机状态
 * @author 44489
 *
 */
public enum MsgManagerState {

	//正常
	SUCCESS(0, "正常"),
	//离线
	OFFLINE(1, "离线");
	
	private int code;
	private String text;
	
	MsgManagerState(int code, String text){
		this.code = code;
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
