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
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.webData.DevCtrlData;
import com.bairock.eleMonitor.service.DeviceService;
import com.bairock.eleMonitor.service.MsgManagerService;

@Controller
public class DevWebSocketController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MsgManagerService msgManagerService;

	@Autowired
	private DeviceService deviceService;

	/**
	 * 网页发出的控制命令
	 * 
	 * @param obj
	 */
	@MessageMapping("/ctrlDev")
	public void ctrlDev(DevCtrlData obj) {
		Device dev = deviceService.findById(obj.getDevId());
		Collector collector = dev.getCollector();
		MsgManager msgManager = collector.getMsgManager();

		byte[] byManagerCode = new byte[4];
		byManagerCode[0] = (byte) (msgManager.getCode() >> 24);
		byManagerCode[1] = (byte) (msgManager.getCode() >> 16);
		byManagerCode[2] = (byte) (msgManager.getCode() >> 8);
		byManagerCode[3] = (byte) (msgManager.getCode());

			byte[] byOne = new byte[7];
			byOne[0] = (byte) collector.getBusCode();
			byOne[1] = (byte) collector.getCode();
			byOne[2] = 0x5;
			byOne[3] = (byte) (dev.getBeginAddress() >> 8);
			byOne[4] = (byte) dev.getBeginAddress();
			byOne[5] = (byte) (dev.getDataLength() >> 8);
			byOne[6] = (byte) dev.getDataLength();
			
			byte[] byData = new byte[dev.getDataLength()];
			for(int i=0; i<byData.length; i++) {
				int step = (byData.length - 1 - i) * 8;
				byData[i] = (byte) (obj.getOption() >> step);
			}

		// 报文, 长度为数据长度加上长度字节数(2) + 通信机号长度(4) + 校验码长度(2)
		int dataLen = byOne.length + byData.length;
		byte[] byTotal = new byte[byManagerCode.length + dataLen + 4];
		byTotal[0] = (byte) (dataLen >> 8);
		byTotal[1] = (byte) dataLen;

		int copyPos = 2;
		System.arraycopy(byManagerCode, 0, byTotal, copyPos, byManagerCode.length);
		copyPos += byManagerCode.length;
		System.arraycopy(byOne, 0, byTotal, copyPos, byOne.length);
		copyPos += byOne.length;
		System.arraycopy(byData, 0, byTotal, copyPos, byData.length);
		//copyPos += byData.length;

		logger.info("ctrl: " + Util.bytesToHexString(byTotal));

		// 发往通信机
		ServerHandler.send(msgManager.getId(), byTotal);
	}

	/**
	 * 网页发出的配置命令
	 * 
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

		// 数据长度
		int totalLen = 0;

		List<byte[]> listBytes = new ArrayList<>();
		for (Collector collector : msgManager.getListCollector()) {
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

		// 报文, 长度为数据长度加上长度字节数(2) + 通信机号长度(4) + 校验码长度(2)
		byte[] byTotal = new byte[totalLen + 8];
		byTotal[0] = (byte) (totalLen >> 8);
		byTotal[1] = (byte) totalLen;

		int copyPos = 2;
		System.arraycopy(byManagerCode, 0, byTotal, copyPos, byManagerCode.length);
		copyPos += byManagerCode.length;

		for (byte[] by : listBytes) {
			System.arraycopy(by, 0, byTotal, copyPos, by.length);
			copyPos += by.length;
		}
		logger.info("config: " + Util.bytesToHexString(byTotal));

		// 发往通信机
		ServerHandler.send(msgManagerId, byTotal);

	}
}
