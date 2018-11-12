package com.bairock.eleMonitor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceGroup;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.repository.DeviceGroupRepository;

@Service
public class DeviceGroupService {

	@Autowired
	private DeviceGroupService self;
	
	@Autowired
	private SubstationService substationService;
	@Autowired
	private DeviceGroupRepository deviceGroupRepository;
	@Autowired
	private CacheManager cacheManager;

	@Cacheable(value = "deviceGroup", key = "#devGroupId")
	public DeviceGroup findById(long devGroupId) {
		Optional<DeviceGroup> option = deviceGroupRepository.findById(devGroupId);
		DeviceGroup dg = option.orElse(null);

		for(Device d : dg.getListDevice()) {
			cacheManager.getCache("device").put(d.getId(), d);
		}

		return dg;
	}

	@CachePut(value = "deviceGroup", key = "#result.id")
	public DeviceGroup addDeviceGroup(long substationId, DeviceGroup devGroup) {
		Substation substation = substationService.findBySubstationId(substationId);
		if (null == substation) {
			return null;
		}
		// 先找到substation, 建立起对应关系
		substation.addDeviceGroup(devGroup);
		deviceGroupRepository.saveAndFlush(devGroup);
		return devGroup;
	}

//	@CachePut(value = "deviceGroup", key = "#result.id")
	public DeviceGroup editDeviceGroup(long devGroupId, DeviceGroup devGroup) {
		DeviceGroup res = self.findById(devGroupId);
		if (null != res) {
			res.setName(devGroup.getName());
			res.setValueType(devGroup.getValueType());
			res.setLineTem(devGroup.isLineTem());
			res.setIcon(devGroup.getIcon());
			deviceGroupRepository.saveAndFlush(res);
		}
		return res;
	}

	public void update(DeviceGroup devGroup) {
		deviceGroupRepository.saveAndFlush(devGroup);
	}

	@CacheEvict(value = "deviceGroup", key = "#result.id")
	public DeviceGroup deleteDeviceGroup(DeviceGroup devGroup) {
		deviceGroupRepository.delete(devGroup);
		return devGroup;
	}
}
