package com.bairock.eleMonitor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.repository.CollectorRepository;

@Service
public class CollectorService {

	@Autowired
	private MsgManagerService msgManagerService;
	@Autowired
	private CollectorRepository collectorRepository;
	
	public Collector findById(long collectorId){
		Optional<Collector> option = collectorRepository.findById(collectorId);
		return option.orElse(null);
	}
	
	public Collector addCollector(long msgManagerId, Collector collector) {
		MsgManager msgManager = msgManagerService.findByMsgManagerId(msgManagerId);
		if(null == msgManager) {
			return null;
		}
		//先找到msgManager, 建立起对应关系
		msgManager.addCollector(collector);
		collectorRepository.saveAndFlush(collector);
		return collector;
	}
	
	public Collector editCollector(long collectorId, Collector collector) {
		Collector res = findById(collectorId);
		if(null != res) {
			res.setName(collector.getName());
			res.setCode(collector.getCode());
			res.setBusCode(collector.getBusCode());
			res.setBeginAddress(collector.getBeginAddress());
			res.setDataLength(collector.getDataLength());
			res.setDataType(collector.getDataType());
			collectorRepository.saveAndFlush(res);
		}
		return res;
	}
	
	/**
	 * 删除采集终端
	 * @param collector
	 * @return
	 */
	public Collector deleteCollector(Collector collector) {
		collectorRepository.delete(collector);
		return collector;
	}
}
