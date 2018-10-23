package com.bairock.eleMonitor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.repository.SubstationRepository;

@Service
public class SubstationService {

	@Autowired
	private SubstationRepository substationRepository;
	@Autowired
	private StationService stationService;
	
	public List<Substation> findByStationId(long stationId){
		List<Substation> listSubstation = substationRepository.findByStationId(stationId);
		return listSubstation;
	}
	
	public Substation findBySubstationId(long substationId){
		Optional<Substation> option = substationRepository.findById(substationId);
		return option.orElse(null);
	}
	
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
	
	public Substation editSubStation(long substationId, Substation substation) {
		Substation sub = findBySubstationId(substationId);
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
	public Substation deleteSubstation(Substation substation) {
		substationRepository.delete(substation);
		return substation;
	}
}
