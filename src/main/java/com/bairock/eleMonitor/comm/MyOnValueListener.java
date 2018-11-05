package com.bairock.eleMonitor.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.Device.OnValueListener;
import com.bairock.eleMonitor.data.DeviceEventMessage;
import com.bairock.eleMonitor.data.ValueType;
import com.bairock.eleMonitor.data.webData.DevWebData;
import com.bairock.eleMonitor.service.DeviceService;

public class MyOnValueListener implements OnValueListener {

	private DeviceService deviceService = SpringUtil.getBean(DeviceService.class);

	@Override
	public void onValueChanged(Device device, float value) {
		DevWebData data = new DevWebData();
		data.setDevId(device.getId());
		data.setValue(device.getValue());
		data.setValueString(device.getValueString());
		if (device.getDeviceGroup() != null) {
			data.setHaveDevGroup(device.getDeviceGroup() != null);
			data.setDevGroupId(device.getDeviceGroup().getId());
		}
		String message = null;
		if (device.getValueType() == ValueType.ALARM) {
			message = device.getName();
			if (device.getValue() == 1) {
				data.setAlarm(true);
				message += " 报警";
			}
		}
		if (device.getValueType() == ValueType.SWITCH) {
			message = device.getName();
			data.setOnOff(true);
			if (device.getValue() == 1) {
				data.setAlarm(true);
				message += " 开";
			} else {
				message += " 关";
			}
		}
		data.setNormal(true);
		deviceService.broadcastValueChanged("admin", data);

		if (null != message) {
			DeviceEventMessage event = new DeviceEventMessage();
			event.setDevice(device);
			event.setEventTime(new Date());
			event.setMessage(message);
			event.setTimeFormat(new SimpleDateFormat("yy-MM-dd HH-mm-ss").format(event.getEventTime()));
			deviceService.broadcastEvent("admin", event);
		}
	}

	@Override
	public void onValueReceived(Device device, float value) {
		// TODO Auto-generated method stub

	}

}
