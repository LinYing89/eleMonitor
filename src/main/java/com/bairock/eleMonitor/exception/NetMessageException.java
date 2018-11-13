package com.bairock.eleMonitor.exception;

import com.bairock.eleMonitor.enums.NetMessageResultEnum;

/**
 * 解析通信机报文的错误信息
 * @author 44489
 *
 */
public class NetMessageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8762260475966219321L;
	
	private int code;
	
	public NetMessageException(NetMessageResultEnum resultEnum) {
		super(resultEnum.getMessage());
		this.code = resultEnum.getCode();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
