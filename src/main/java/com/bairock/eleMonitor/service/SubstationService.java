package com.bairock.eleMonitor.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceEventMessage;
import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.repository.SubstationRepository;

@Service
public class SubstationService {

	@Resource
	private SubstationService self;
	
	@Autowired
	private SubstationRepository substationRepository;
	@Autowired
	private StationService stationService;
	@Autowired
	private DeviceEventMessageService deviceEventMessageService;
	
	public List<Substation> findByStationId(long stationId){
		List<Substation> listSubstation = substationRepository.findByStationId(stationId);
		return listSubstation;
	}
	
	@Cacheable(value="substation", key="#substationId")
	public Substation findBySubstationId(long substationId){
		Optional<Substation> option = substationRepository.findById(substationId);
		Substation s = option.orElse(null);
		return s;
	}
	
	/**
	 * 先清空缓存, 再重新读取放入缓存
	 * @param substationId
	 */
	public void reloadCache(long substationId) {
//		Optional<Substation> option = substationRepository.findById(substationId);
//		Substation s = option.orElse(null);
//		cacheManager.getCache("substation").evict(substationId);
//		if(null != s) {
//			for(MsgManager m : s.getListMsgManager()) {
//				cacheManager.getCache("msgmanager").evict(m.getCode());
//				for(Collector c : m.getListCollector()) {
//					cacheManager.getCache("collector").evict(c.getId());
//					for(Device d : c.getListDevice()) {
//						cacheManager.getCache("device").evict(d.getId());
//					}
//				}
//			}
//		}
//		
//		self.findBySubstationId(substationId);
	}
	
	@CachePut(value="substation",key="#result.id")
	public Substation addSubStation(long stationId, Substation substation) {
		Station station = stationService.findStation(stationId);
		if(null == station) {
			return null;
		}
		//先找到station, 建立起对应关系, 否则substation没有stationid
		station.addSubstation(substation);
		substationRepository.saveAndFlush(substation);
		return substation;
	}
	
//	@CachePut(value="substation",key="#result.id")
	public Substation editSubStation(long substationId, Substation substation) {
		Substation sub = self.findBySubstationId(substationId);
		if(null != sub) {
			sub.setName(substation.getName());
			substationRepository.saveAndFlush(sub);
		}
		return sub;
	}
	
	/**
	 * 删除变电所
	 * @param substationId
	 * @return
	 */
	@CacheEvict(value="substation", key="#result.id")
	public Substation deleteSubstation(Substation substation) {
		substationRepository.delete(substation);
		return substation;
	}
	
	public List<DeviceEventMessage> findAllEvent(Substation substation){
		List<DeviceEventMessage> list = new ArrayList<DeviceEventMessage>();
		for(Device dev : substation.findDevices()) {
			list.addAll(deviceEventMessageService.findTodayByDeviceId(dev.getId()));
		}
		Collections.sort(list);
		if(list.size() > 30) {
			return list.subList(0, 30);
		}
		return list;
	}
}
