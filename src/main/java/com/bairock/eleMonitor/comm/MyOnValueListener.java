package com.bairock.eleMonitor.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.Device.OnValueListener;
import com.bairock.eleMonitor.data.DeviceEventMessage;
import com.bairock.eleMonitor.data.DeviceValueHistory;
import com.bairock.eleMonitor.data.ValueType;
import com.bairock.eleMonitor.data.webData.DevWebData;
import com.bairock.eleMonitor.service.DeviceEventMessageService;
import com.bairock.eleMonitor.service.DeviceService;
import com.bairock.eleMonitor.service.DeviceValueHistoryService;

public class MyOnValueListener implements OnValueListener {

	private DeviceService deviceService = SpringUtil.getBean(DeviceService.class);
	private DeviceEventMessageService eventService = SpringUtil.getBean(DeviceEventMessageService.class);
	private DeviceValueHistoryService historyService = SpringUtil.getBean(DeviceValueHistoryService.class);

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

		if(device.isAlarming()) {
			data.setAlarm(true);
			message = device.getName();
			message += " 报警";
		}
		if (device.getValueType() == ValueType.SWITCH) {
			message = device.getName();
			data.setOnOff(true);
			if (device.getValue() == 1) {
				// data.setAlarm(true);
				message += " 开";
			} else {
				message += " 关";
			}
		}
		data.setNormal(true);
		long substationId = device.getCollector().getMsgManager().getSubstation().getId();
		deviceService.broadcastValueChanged(substationId, data);

		try {
			if (null != message) {
				DeviceEventMessage event = new DeviceEventMessage();
				event.setDevice(device);
				event.setEventTime(new Date());
				event.setMessage(message);
				event.setAlarm(data.isAlarm());
				event.setTimeFormat(new SimpleDateFormat("yy-MM-dd HH-mm-ss").format(event.getEventTime()));
//				device.addEventMessage(event);
				eventService.add(event);
				deviceService.broadcastEvent(substationId, event);
			}
			device.getCollector().getMsgManager().getSubstation().getStation().refreshState();
		} catch (Exception e) {

		}
	}

	@Override
	public void onValueReceived(Device device, float value) {
		DeviceValueHistory devHistory = new DeviceValueHistory();
		devHistory.setDevice(device);
		devHistory.setTime(new Date());
		devHistory.setValue(value);
//		device.addValueHistory(devHistory);
		historyService.add(devHistory);
		
	}

}
