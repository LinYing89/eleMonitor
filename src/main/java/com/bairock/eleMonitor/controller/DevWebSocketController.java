package com.bairock.eleMonitor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.webData.DevCtrlData;
import com.bairock.eleMonitor.service.DeviceService;
import com.bairock.eleMonitor.service.MsgManagerService;
import com.bairock.eleMonitor.service.SendService;

@Controller
public class DevWebSocketController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MsgManagerService msgManagerService;

	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private SendService sendService;

	/**
	 * 网页发出的控制命令
	 * 
	 * @param obj
	 */
	@MessageMapping("/ctrlDev")
	public void ctrlDev(DevCtrlData obj) {
		Device dev = deviceService.findById(obj.getDevId());
		sendService.ctrlDev(dev, obj.getOption());
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

		sendService.configDev(msgManager);
	}
}
