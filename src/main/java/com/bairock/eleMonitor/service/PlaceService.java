package com.bairock.eleMonitor.service;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.Place;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.repository.PlaceRepo;

@Service
public class PlaceService {
	
	@Resource
	private PlaceService self;
	
	@Autowired
	private PlaceRepo placeRepo;
	@Autowired
	private SubstationService substationService;
	@Autowired
	private DeviceService deviceService;
	
	@Cacheable(value = "place", key = "#placeId")
	public Place findById(long placeId) {
		// 获取数据库中的对象
		Optional<Place> option = placeRepo.findById(placeId);
		Place c = option.orElse(null);
		return c;
	}

	@CachePut(value = "place", key = "#result.id")
	public Place addPlace(long substationId, Place place) {
		Substation substation = substationService.findBySubstationId(substationId);
		substation.addPlace(place);
		placeRepo.save(place);
		return place;
	}

//	@CachePut(value = "collector", key = "#result.id")
	public Place editPlace(long placeId, Place place) {
		Place placeDb = self.findById(placeId);
		placeDb.setName(place.getName());
		placeRepo.saveAndFlush(placeDb);
//		Substation substation = substationService.findBySubstationId(placeDb.getSubstation().getId());
//		for(Place p : substation.getListPlace()) {
//			if(p.getId() == placeId) {
//				p.setName(place.getName());
//				break;
//			}
//		}
		return placeDb;
	}

	@CacheEvict(value = "place", key = "#placeId")
	public Place deletePlace(long placeId) {
		Place place = self.findById(placeId);
		Substation substation = substationService.findBySubstationId(place.getSubstation().getId());
		substation.removePlaceById(placeId);
		for(MsgManager mm : substation.getListMsgManager()) {
			for(Collector c : mm.getListCollector()) {
				for(Device d : c.getListDevice()) {
					if(d.getPlace().getId() == placeId) {
						d.setPlace(null);
						deviceService.update(d);
					}
				}
			}
		}
		placeRepo.deleteById(placeId);
		
		return place;
	}
	
}
