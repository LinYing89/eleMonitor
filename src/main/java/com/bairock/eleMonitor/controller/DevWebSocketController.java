package com.bairock.eleMonitor.controller;

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
import com.bairock.eleMonitor.data.webData.NetMessageSentResult;
import com.bairock.eleMonitor.service.DeviceService;
import com.bairock.eleMonitor.service.MsgManagerService;
import com.bairock.eleMonitor.service.TestService;

@Controller
public class DevWebSocketController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MsgManagerService msgManagerService;

	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private TestService testService;

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

//			byte[] byOne = new byte[7];
			byte[] byOne = new byte[5];
			byOne[0] = (byte) collector.getBusCode();
			byOne[1] = (byte) collector.getCode();
			byOne[2] = 0x5;
			byOne[3] = (byte) (dev.getBeginAddress() >> 8);
			byOne[4] = (byte) dev.getBeginAddress();
//			byOne[5] = (byte) (dev.getDataLength() >> 8);
//			byOne[6] = (byte) dev.getDataLength();
//			byOne[5] = (byte) (dev.getDataLength() >> 8);
//			byOne[6] = 2;
			
//			byte[] byData = new byte[dev.getDataLength()];
//			for(int i=0; i<byData.length; i++) {
//				int step = (byData.length - 1 - i) * 8;
//				byData[i] = (byte) (obj.getOption() >> step);
//			}
			byte[] byData = new byte[2];
			if(obj.getOption() == 1) {
				byData[0] = (byte) 0xff;
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
		sendMessage(msgManager.getId(), byTotal);
//		ServerHandler.send(msgManager.getId(), byTotal);
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
		byte[] byHead = new byte[7];
		byHead[2] = 0x40;
		
		byte[] byAllData = new byte[msgManager.getListCollector().size() * 8];
		byHead[6] = (byte) byAllData.length;
		int pos = 0;
		for (Collector collector : msgManager.getListCollector()) {
			byte[] byData = new byte[8];
			
			byData[0] = (byte) collector.getCode();
			byData[1] = (byte) collector.getFunctionCode();
			
			byData[2] = (byte) (collector.getBeginAddress() >> 8);
			byData[3] = (byte) collector.getBeginAddress();
			
			int dataLen = collector.getDataLength();
			//如果时开关量设备, 要把位数转为字数
			if(collector.getFunctionCode() == 01 || collector.getFunctionCode() == 02) {
				int zi = dataLen / 16;
				if(dataLen % 16 != 0) {
					zi += 1;
				}
				dataLen = zi;
			}
			
			byData[4] = (byte) (dataLen >> 8);
			byData[5] = (byte) dataLen;
			
			byData[6] = (byte) (dataLen * 2);
			
//			System.arraycopy(byHead, 0, byOne, 0, byHead.length);
			System.arraycopy(byData, 0, byAllData, pos, byData.length);
			pos += 8;
		}
		totalLen = byHead.length + byAllData.length;
		// 报文, 长度为数据长度加上长度字节数(2) + 通信机号长度(4) + 校验码长度(2)
		byte[] byTotal = new byte[totalLen + 8];
		byTotal[0] = (byte) (totalLen >> 8);
		byTotal[1] = (byte) totalLen;

		int copyPos = 2;
		System.arraycopy(byManagerCode, 0, byTotal, copyPos, byManagerCode.length);
		copyPos += byManagerCode.length;
		System.arraycopy(byHead, 0, byTotal, copyPos, byHead.length);
		copyPos += byHead.length;
		System.arraycopy(byAllData, 0, byTotal, copyPos, byAllData.length);

//		for (byte[] by : listBytes) {
//			System.arraycopy(by, 0, byTotal, copyPos, by.length);
//			copyPos += by.length;
//		}
		logger.info("config: " + Util.bytesToHexString(byTotal));

		// 发往通信机
		sendMessage(msgManagerId, byTotal);
//		ServerHandler.send(msgManagerId, byTotal);

	}
	
	private void sendMessage(long msgManagerId, byte[] byTotal) {
		NetMessageSentResult result = ServerHandler.send(msgManagerId, byTotal);
		testService.broadcastSent(result);
	}
}
