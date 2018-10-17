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
	public Station findStation(long stationId) {
		Optional<Station> option = stationRepository.findById(stationId);
		return option.orElse(null);
	}
	
	public List<Station> findAll(){
		return stationRepository.findAll();
	}
	
	public Station save(Station station){
		return stationRepository.saveAndFlush(station);
	}
	
	public void deleteById(long stationId){
		stationRepository.deleteById(stationId);
	}
}
