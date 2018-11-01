package com.bairock.eleMonitor.service;

import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.comm.MyServer;

@Service
public class MsgManagerServerService {

	public MsgManagerServerService() {
		MyServer myServer = new MyServer();
		try {
			myServer.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
