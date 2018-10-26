package com.bairock.eleMonitor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.DeviceGroup;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.repository.DeviceGroupRepository;

@Service
public class DeviceGroupService {

	@Autowired
	private SubstationService substationService;
	@Autowired
	private DeviceGroupRepository deviceGroupRepository;
	
	public DeviceGroup findById(long devGroupId){
		Optional<DeviceGroup> option = deviceGroupRepository.findById(devGroupId);
		return option.orElse(null);
	}
	
	public DeviceGroup addDeviceGroup(long substationId, DeviceGroup devGroup) {
		Substation substation = substationService.findBySubstationId(substationId);
		if(null == substation) {
			return null;
		}
		//先找到substation, 建立起对应关系
		substation.addDeviceGroup(devGroup);
		deviceGroupRepository.saveAndFlush(devGroup);
		return devGroup;
	}
	
	public DeviceGroup editDeviceGroup(long devGroupId, DeviceGroup devGroup) {
		DeviceGroup res = findById(devGroupId);
		if(null != res) {
			res.setName(devGroup.getName());
			res.setIcon(devGroup.getIcon());
			deviceGroupRepository.saveAndFlush(res);
		}
		return res;
	}
	
	public void update(DeviceGroup devGroup) {
		deviceGroupRepository.saveAndFlush(devGroup);
	}
	
	public DeviceGroup deleteDeviceGroup(DeviceGroup devGroup) {
		deviceGroupRepository.delete(devGroup);
		return devGroup;
	}
}
