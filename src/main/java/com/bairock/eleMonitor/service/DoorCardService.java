package com.bairock.eleMonitor.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DoorAuthority;
import com.bairock.eleMonitor.data.DoorCard;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.repository.DoorCardRepository;

@Service
public class DoorCardService {

	@Resource
	private DoorCardService self;
	
	@Autowired
	private DoorCardRepository doorCardRepository;
	
	@Autowired
	private SubstationService substationService;
	@Autowired
	private DeviceService deviceService;
	
	public DoorCard findById(long cardId) {
		return doorCardRepository.findById(cardId).orElse(null);
	}
	
	public List<DoorCard> findBySubstationId(long substationId) {
		return doorCardRepository.findBySubstationId(substationId);
	}
	
	public DoorCard addDoorCard(long substationId, DoorCard doorCard) {
		Substation substation = substationService.findBySubstationId(substationId);
		if (null == substation) {
			return null;
		}
		//建立起对应关系
		substation.addDoorCard(doorCard);
		doorCardRepository.saveAndFlush(doorCard);
		return doorCard;
	}
	
	public DoorCard editDoorCard(long doorCardId, DoorCard doorCard) {
		DoorCard dc = self.findById(doorCardId);
		dc.setCardNum(doorCard.getCardNum());
		dc.setUsername(doorCard.getUsername());
		dc.setLimitTimeStart(doorCard.getLimitTimeStart());
		dc.setLimitTimeEnd(doorCard.getLimitTimeEnd());
		dc.getListDoorAuthority().clear();
		for(DoorAuthority da : doorCard.getListDoorAuthority()) {
			Device device = deviceService.findById(da.getDeviceId());
			da.setDevice(device);
			dc.addDoorAuthority(da);
		}
		doorCardRepository.saveAndFlush(dc);
		return dc;
	}
	
	public List<DoorAuthority> getDoorAuthority(long cardId){
		Optional<DoorCard> card = doorCardRepository.findById(cardId);
		DoorCard doorCard = card.orElse(null);
		List<DoorAuthority> listDoorAuthority = doorCard.getListDoorAuthority();
		Substation substation = doorCard.getSubstation();
		List<Device> listDevice = substation.findDoorDevice();
		for(Device device : listDevice) {
			boolean haved = false;
			for(DoorAuthority doorAuthority : listDoorAuthority) {
				if(doorAuthority.getDevice().getName().equals(device.getName())) {
					haved = true;
					break;
				}
			}
			if(!haved) {
				DoorAuthority doorAuthority = new DoorAuthority();
				doorAuthority.setDevice(device);
				doorAuthority.setDeviceId(device.getId());
				doorCard.addDoorAuthority(doorAuthority);
			}
		}
		return doorCard.getListDoorAuthority();
	}
}
