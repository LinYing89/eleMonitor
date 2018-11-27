package com.bairock.eleMonitor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.Effect;
import com.bairock.eleMonitor.data.Linkage;
import com.bairock.eleMonitor.repository.EffectRepository;

@Service
public class EffectService {

	@Autowired
	private EffectService self;
	
	@Autowired
	private EffectRepository effectRepository;
	@Autowired
	private LinkageService linkageService;
	@Autowired
	private DeviceService deviceService;
	
	@Cacheable(value = "effect", key = "#id")
	public Effect findById(long id) {
		Optional<Effect> option = effectRepository.findById(id);
		Effect d = option.orElse(null);
		return d;
	}
	
	@CachePut(value = "effect", key = "#result.id")
	public Effect addEffect(long linkageId, long deviceId, float value) {
		Linkage linkage = linkageService.findById(linkageId);
		Device device = deviceService.findById(deviceId);
		if(null == linkage || null == device) {
			return null;
		}
		Effect effect = new Effect();
		effect.setDevice(device);
		effect.setValue(value);
		linkage.addEffect(effect);
		effectRepository.saveAndFlush(effect);
		return effect;
	}
	
	public Effect editEffect(long effectId, long deviceId, float value) {
		Effect res = self.findById(effectId);
		Device device = deviceService.findById(deviceId);
		if(null != res) {
			res.setDevice(device);
			res.setValue(value);
			effectRepository.saveAndFlush(res);
		}
		return res;
	}
	
	@CacheEvict(value = "effect", key = "#result.id")
	public Effect delete(Effect effect) {
		effectRepository.delete(effect);
		return effect;
	}
	
}
