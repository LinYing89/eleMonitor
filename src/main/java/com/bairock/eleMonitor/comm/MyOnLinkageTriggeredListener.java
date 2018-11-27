package com.bairock.eleMonitor.comm;

import java.util.List;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.Device.OnLinkageTriggeredListener;
import com.bairock.eleMonitor.data.Effect;
import com.bairock.eleMonitor.service.SendService;

/**
 * 连锁触发监听
 * @author 44489
 *
 */
public class MyOnLinkageTriggeredListener implements OnLinkageTriggeredListener {

	private SendService sendService = SpringUtil.getBean(SendService.class);
	
	@Override
	public void onLinkageTriggered(Device device, List<Effect> listEffect) {
		for(Effect effect : listEffect) {
			if(effect.getDevice().getValue() != effect.getValue()) {
				sendService.ctrlDev(effect.getDevice(), (int)effect.getValue());
			}
		}
	}

}
