package com.bairock.eleMonitor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.repository.MsgManagerRepository;

@Service
public class MsgManagerService {

	@Autowired
	private MsgManagerService self;

	@Autowired
	private SubstationService substationService;
	@Autowired
	private MsgManagerRepository msgManagerRepository;
	@Autowired
	private CacheManager cacheManager;

	public MsgManager findByMsgManagerId(long msgManagerId) {
		Optional<MsgManager> option = msgManagerRepository.findById(msgManagerId);
		MsgManager mm = option.orElse(null);
		if (null != mm) {
			// 如果可以, 从缓存中获取对象
			return self.findByMsgManagerCode(mm.getCode());
		}
		return mm;
	}

	@Cacheable(value = "msgmanager", key = "#msgManagerCode")
	public MsgManager findByMsgManagerCode(int msgManagerCode) {
		Optional<MsgManager> option = msgManagerRepository.findByCode(msgManagerCode);
		MsgManager mm = option.orElse(null);

		if (mm != null) {
			for (Collector c : mm.getListCollector()) {
				cacheManager.getCache("collector").put(c.getId(), c);
			}
		}
		return mm;
	}
	
	private boolean codeIsHaved(int code) {
		Optional<MsgManager> option = msgManagerRepository.findByCode(code);
		MsgManager mm = option.orElse(null);
		if(mm == null) {
			return false;
		}
		return true;
	}

	@CachePut(value = "msgmanager", condition = "#result!=null", key = "#result.code")
	public MsgManager addMsgManager(long substationId, MsgManager msgManager) {
		if (codeIsHaved(msgManager.getCode())) {
			// 已有同号的通信机
			return null;
		}

		Substation substation = substationService.findBySubstationId(substationId);
		if (null == substation) {
			return null;
		}
		// 先找到substation, 建立起对应关系
		substation.addMsgManager(msgManager);
		msgManagerRepository.saveAndFlush(msgManager);
		return msgManager;
	}

//	@CachePut(value = "msgmanager", condition = "#result!=null", key = "#result.code")
	public MsgManager editMsgManager(long msgManagerId, MsgManager msgManager) {
		MsgManager res = self.findByMsgManagerId(msgManagerId);
		if (null != res) {
			if (res.getCode() != msgManager.getCode()) {
				// 如果改变了通信机的编号, 则要查询有没有已存在的同号的通信机
				if (codeIsHaved(msgManager.getCode())) {
					// 已有同号的通信机
					return null;
				}
			}
			res.setName(msgManager.getName());
			res.setCode(msgManager.getCode());
			res.setPlace(msgManager.getPlace());
			msgManagerRepository.saveAndFlush(res);
		}
		return res;
	}

	/**
	 * 删除通信机
	 * 
	 * @param msgManager
	 * @return
	 */
	@CacheEvict(value = "msgmanager", key = "#result.code")
	public MsgManager deleteMsgManager(MsgManager msgManager) {
		msgManagerRepository.delete(msgManager);
		return msgManager;
	}

}
