package com.bairock.eleMonitor.comm;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.Device.OnValueChangedListener;
import com.bairock.eleMonitor.data.webData.DevWebData;
import com.bairock.eleMonitor.service.DeviceService;

public class MyOnValueChangedListener implements OnValueChangedListener {

	private DeviceService deviceService = SpringUtil.getBean(DeviceService.class);

	@Override
	public void onValueChanged(Device device, float value) {
		DevWebData data = new DevWebData();
		data.setDevId(device.getId());
		data.setValue(device.getValue());
		if (device.getDeviceGroup() != null) {
			data.setHaveDevGroup(device.getDeviceGroup() != null);
			data.setDevGroupId(device.getDeviceGroup().getId());
		}
		data.setNormal(true);
		deviceService.broadcastValueChanged("admin", data);
	}

}
