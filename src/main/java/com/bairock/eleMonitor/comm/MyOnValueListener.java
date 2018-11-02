package com.bairock.eleMonitor.comm;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.Device.OnValueListener;
import com.bairock.eleMonitor.data.ValueType;
import com.bairock.eleMonitor.data.webData.DevWebData;
import com.bairock.eleMonitor.service.DeviceService;

public class MyOnValueListener implements OnValueListener {

	private DeviceService deviceService = SpringUtil.getBean(DeviceService.class);

	@Override
	public void onValueChanged(Device device, float value) {
		DevWebData data = new DevWebData();
		data.setDevId(device.getId());
		data.setValue(device.getValueString());
		if (device.getDeviceGroup() != null) {
			data.setHaveDevGroup(device.getDeviceGroup() != null);
			data.setDevGroupId(device.getDeviceGroup().getId());
		}
		if(device.getValueType() == ValueType.ALARM) {
			if(device.getValue() == 1) {
				data.setAlarm(true);
			}
		}
		data.setNormal(true);
		deviceService.broadcastValueChanged("admin", data);
	}

	@Override
	public void onValueReceived(Device device, float value) {
		// TODO Auto-generated method stub
		
	}

}
