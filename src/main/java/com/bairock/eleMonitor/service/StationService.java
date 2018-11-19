package com.bairock.eleMonitor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.comm.MyOnStationStateChangedListener;
import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceGroup;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.enums.StationState;
import com.bairock.eleMonitor.repository.StationRepository;

@Service
public class StationService {

	@Resource
	private StationService self;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 获取一个站对象
	 * @param stationId
	 * @return
	 */
	@Cacheable(value="station", key="#stationId")
	public Station findStation(long stationId) {
		Optional<Station> option = stationRepository.findById(stationId);
		Station station = option.orElse(null);
		if(null == station) {
			return null;
		}
		cacheManager.getCache("station").put(station.getId(), station);
		station.setOnStateChangedListener(new MyOnStationStateChangedListener());
		for(Substation substation : station.getListSubstation()) {
			cacheManager.getCache("substation").put(substation.getId(), substation);
			for(MsgManager m : substation.getListMsgManager()) {
				cacheManager.getCache("msgmanager").put(m.getCode(), m);
				for(Collector c : m.getListCollector()) {
					cacheManager.getCache("collector").put(c.getId(), c);
					for(Device d : c.getListDevice()) {
						cacheManager.getCache("device").put(d.getId(), d);
						if(d.isAlarming()) {
							station.setState(StationState.ALARM);
						}
					}
				}
			}

			for(DeviceGroup dg : substation.getListDeviceGroup()) {
				cacheManager.getCache("deviceGroup").put(dg.getId(), dg);
			}
		}
		return station;
	}
	
	public List<Station> findAllByUserId(long userId) {
		List<Station> listDb = stationRepository.findAllByUserId(userId);
		List<Station> list = new ArrayList<Station>();
		for(Station station : listDb) {
			Station s = self.findStation(station.getId());
			if(null != s) {
				list.add(s);
			}
		}
		return list;
	}
	
	public List<Station> findAll(){
		List<Station> listDb = stationRepository.findAll();
		List<Station> list = new ArrayList<Station>();
		for(Station station : listDb) {
			Station s = self.findStation(station.getId());
			if(null != s) {
				list.add(s);
			}
		}
		return list;
	}
	
	@CachePut(value = "station",key="#result.id")
	public Station save(Station station){
		return stationRepository.saveAndFlush(station);
	}
	
//	@CachePut(value = "station",key="#result.id")
	public Station edit(long stationId, Station station) {
		Station res = self.findStation(stationId);
		if(null != res) {
			res.setName(station.getName());
			res.setAddress(station.getAddress());
			res.setLat(station.getLat());
			res.setLng(station.getLng());
			res.setTel(station.getTel());
			res.setRemark(station.getRemark());
			stationRepository.saveAndFlush(res);
		}
		return res;
	}
	
	@CacheEvict(value = "station", key="#stationId")
	public void deleteById(long stationId){
		stationRepository.deleteById(stationId);
	}
}
