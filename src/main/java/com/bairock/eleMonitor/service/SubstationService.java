package com.bairock.eleMonitor.service;

import java.util.List;
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
import com.bairock.eleMonitor.data.DeviceGroup;
import com.bairock.eleMonitor.data.MsgManager;
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
	private CacheManager cacheManager;
	
	public List<Substation> findByStationId(long stationId){
		List<Substation> listSubstation = substationRepository.findByStationId(stationId);
		return listSubstation;
	}
	
	@Cacheable(value="substation", key="#substationId")
	public Substation findBySubstationId(long substationId){
		Optional<Substation> option = substationRepository.findById(substationId);
		Substation s = option.orElse(null);
		//初始化manager并缓存
		//尝试获取缓存中的manager, 否则可能导致netty handler中获取的manager和这里获取的manager不是一个
		//有可能netty handler中先获取了manager, 放入了缓存, 这里又从数据库中读取了一次新的对象放在了substation对象中
		for(MsgManager m : s.getListMsgManager()) {
			cacheManager.getCache("msgmanager").put(m.getCode(), m);
			for(Collector c : m.getListCollector()) {
				cacheManager.getCache("collector").put(c.getId(), c);
				for(Device d : c.getListDevice()) {
					cacheManager.getCache("device").put(d.getId(), d);
				}
			}
		}

		for(DeviceGroup dg : s.getListDeviceGroup()) {
			cacheManager.getCache("deviceGroup").put(dg.getId(), dg);
		}
		return s;
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
}
