package com.bairock.eleMonitor.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.bairock.eleMonitor.Util;
import com.bairock.eleMonitor.comm.ServerHandler;
import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.service.MsgManagerService;

@Controller
public class DevWebSocketController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MsgManagerService msgManagerService;
	
	/**
	 * 网页发出的控制命令
	 * @param obj
	 */
	@MessageMapping("/ctrlDev")
	public void ctrlDev(Object obj) {
		
	}
	
	/**
	 * 网页发出的配置命令
	 * @param obj
	 */
	@MessageMapping("/configDev")
	public void configDev(long msgManagerId) {
		logger.info("msgManagerId=" + msgManagerId);
		MsgManager msgManager = msgManagerService.findByMsgManagerId(msgManagerId);
		
		byte[] byManagerCode = new byte[4];
		byManagerCode[0] = (byte) (msgManager.getCode() >> 24);
		byManagerCode[1] = (byte) (msgManager.getCode() >> 16);
		byManagerCode[2] = (byte) (msgManager.getCode() >> 8);
		byManagerCode[3] = (byte) (msgManager.getCode());
		
		//数据长度
		int totalLen = 4;
		
		List<byte[]> listBytes = new ArrayList<>();
		for(Collector collector : msgManager.getListCollector()) {
			byte[] byOne = new byte[7 + collector.getDataLength()];
			byOne[0] = (byte) collector.getBusCode();
			byOne[1] = (byte) collector.getCode();
			byOne[2] = (byte) collector.getFunctionCode();
			byOne[3] = (byte) (collector.getBeginAddress() >> 8);
			byOne[4] = (byte) collector.getBeginAddress();
			byOne[5] = (byte) (collector.getDataLength() >> 8);
			byOne[6] = (byte) collector.getDataLength();
			listBytes.add(byOne);
			totalLen += byOne.length;
		}
		
		//报文, 长度为数据长度加上长度字节数(2) + 校验码长度(2)
		byte[] byTotal = new byte[totalLen + 4];
		byTotal[0] = (byte) (totalLen >> 8);
		byTotal[1] = (byte) totalLen;
		
		int copyPos = 2;
		System.arraycopy(byManagerCode, 0, byTotal, copyPos, byManagerCode.length);
		copyPos += byManagerCode.length;
		
		for(byte[] by : listBytes) {
			System.arraycopy(by, 0, byTotal, copyPos, by.length);
			copyPos += by.length;
		}
		logger.info("config: " + Util.bytesToHexString(byTotal));
		
		//发往通信机
		ServerHandler.send(msgManagerId, byTotal);
		
	}
}
