package com.bairock.eleMonitor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.repository.MsgManagerRepository;

@Service
public class MsgManagerService {

	@Autowired
	private SubstationService substationService;
	@Autowired
	private MsgManagerRepository msgManagerRepository;
	
	public MsgManager findByMsgManagerId(long msgManagerId){
		Optional<MsgManager> option = msgManagerRepository.findById(msgManagerId);
		return option.orElse(null);
	}
	
	public MsgManager addMsgManager(long substationId, MsgManager msgManager) {
		Substation substation = substationService.findBySubstationId(substationId);
		if(null == substation) {
			return null;
		}
		//先找到substation, 建立起对应关系
		substation.addMsgManager(msgManager);
		msgManagerRepository.saveAndFlush(msgManager);
		return msgManager;
	}
	
	public MsgManager editMsgManager(long msgManagerId, MsgManager msgManager) {
		MsgManager res = findByMsgManagerId(msgManagerId);
		if(null != res) {
			res.setName(msgManager.getName());
			res.setCode(msgManager.getCode());
			res.setPlace(msgManager.getPlace());
			msgManagerRepository.saveAndFlush(res);
		}
		return res;
	}
	
	/**
	 * 删除通信机
	 * @param msgManager
	 * @return
	 */
	public MsgManager deleteMsgManager(MsgManager msgManager) {
		msgManagerRepository.delete(msgManager);
		return msgManager;
	}
	
}
