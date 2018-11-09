package com.bairock.eleMonitor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.repository.StationRepository;

@Service
public class StationService {

	@Autowired
	private StationRepository stationRepository;
	
	/**
	 * 获取一个站对象
	 * @param stationId
	 * @return
	 */
//	@Cacheable(value="station", key="#stationId")
	public Station findStation(long stationId) {
		Optional<Station> option = stationRepository.findById(stationId);
		return option.orElse(null);
	}
	
	public List<Station> findAll(){
		return stationRepository.findAll();
	}
	
//	@CachePut(value = "station",key="#result.id")
	public Station save(Station station){
		return stationRepository.saveAndFlush(station);
	}
	
//	@CachePut(value = "station",key="#result.id")
	public Station edit(long stationId, Station station) {
		Station res = findStation(stationId);
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
	
//	@CacheEvict(value = "station", key="#stationId")
	public void deleteById(long stationId){
		stationRepository.deleteById(stationId);
	}
}
