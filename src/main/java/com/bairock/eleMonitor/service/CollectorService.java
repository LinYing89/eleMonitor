package com.bairock.eleMonitor.service;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.repository.CollectorRepository;

@Service
public class CollectorService {

	@Resource
	private CollectorService self;

	@Autowired
	private MsgManagerService msgManagerService;
	@Autowired
	private CollectorRepository collectorRepository;
	@Autowired
	private CacheManager cacheManager;

	@Cacheable(value = "collector", key = "#collectorId")
	public Collector findById(long collectorId) {
		// 获取数据库中的对象
		Optional<Collector> option = collectorRepository.findById(collectorId);
		Collector c = option.orElse(null);

		// 初始化Device并缓存
		for (Device d : c.getListDevice()) {
			cacheManager.getCache("device").put(d.getId(), d);
		}
		return c;
	}

	@CachePut(value = "collector", key = "#result.id")
	public Collector addCollector(long msgManagerId, Collector collector) {
		MsgManager msgManager = msgManagerService.findByMsgManagerId(msgManagerId);
		if (null == msgManager) {
			return null;
		}
		// 先找到msgManager, 建立起对应关系
		msgManager.addCollector(collector);
		collectorRepository.saveAndFlush(collector);
		return collector;
	}

	@CachePut(value = "collector", key = "#result.id")
	public Collector editCollector(long collectorId, Collector collector) {
		Collector res = self.findById(collectorId);
		if (null != res) {
			res.setName(collector.getName());
			res.setCode(collector.getCode());
			res.setBusCode(collector.getBusCode());
			res.setFunctionCode(collector.getFunctionCode());
			res.setBeginAddress(collector.getBeginAddress());
			res.setDataLength(collector.getDataLength());
			res.setDataType(collector.getDataType());
			collectorRepository.saveAndFlush(res);
		}
		return res;
	}

	/**
	 * 删除采集终端
	 * 
	 * @param collector
	 * @return
	 */
	@CacheEvict(value = "collector", key = "#collectorId")
	public Collector deleteCollector(long collectorId) {
		Collector res = self.findById(collectorId);
		collectorRepository.delete(res);
		return res;
	}
}
