package com.bairock.eleMonitor.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceValueHistory;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.service.DeviceValueHistoryService;
import com.bairock.eleMonitor.service.SubstationService;

@Controller
@RequestMapping("/valueHistory")
public class DeviceValueHistoryController {

	@Autowired
	private DeviceValueHistoryService deviceValueHistoryService;
	@Autowired
	private SubstationService substationService;

	@GetMapping("/{substationId}/{deviceId}")
	public String getHistory(@PathVariable long substationId, @PathVariable long deviceId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		List<Device> listValueDevice = substation.findAllValueDevice();
		List<Device> listCtrlDevice = substation.findAllCtrlDevice();
//		for (Device dev : listValueDevice) {
//			List<DeviceValueHistory> list = deviceValueHistoryController.findAllByDeviceId(dev.getId());
//			dev.setListValueHistory(list);
//		}
//		for (Device dev : listCtrlDevice) {
//			List<DeviceValueHistory> list = deviceValueHistoryController.findAllByDeviceId(dev.getId());
//			dev.setListValueHistory(list);
//		}
		model.addAttribute("substation", substation);
		model.addAttribute("listCtrlDevice", listCtrlDevice);
		model.addAttribute("listValueDevice", listValueDevice);
		Device mainDevice = null;
		if (!listValueDevice.isEmpty()) {
			mainDevice = listValueDevice.get(0);
		} else if (listCtrlDevice.isEmpty()) {
			mainDevice = listCtrlDevice.get(0);
		}
		model.addAttribute("mainDevice", mainDevice);
		return "device/deviceHistory";
	}

//	@ResponseBody
//	@GetMapping("/dev/{deviceId}")
//	public List<ChartData> getValueHistory(@PathVariable long deviceId) {
//		List<DeviceValueHistory> list = deviceValueHistoryController.findAllByDeviceId(deviceId);
//		Collections.sort(list);
//		List<ChartData> listChartData = new ArrayList<>();
//		for(DeviceValueHistory history : list) {
//			ChartData data = new ChartData();
//			data.setTime(history.timeStr());
//			data.setName(history.getDevice().getName());
//			data.setValue(history.getValue());
//			listChartData.add(data);
//		}
//		return listChartData;
//		
//	}

	@ResponseBody
	@GetMapping("/dev/{deviceId}")
	public Object[][] getValueHistory(@PathVariable long deviceId) {
		List<DeviceValueHistory> list = deviceValueHistoryService.findTodayByDeviceId(deviceId);
		if (null == list || list.isEmpty()) {
			return new Object[0][0];
		}
		Collections.sort(list);
		Object[][] values = new Object[2][list.size() + 1];
		values[0][0] = "device";
		values[1][0] = list.get(0).getDevice().getName();
		for (int i = 0; i < list.size(); i++) {
			DeviceValueHistory history = list.get(i);
			values[0][i + 1] = history.timeStr();
			values[1][i + 1] = history.getValue();
		}
		return values;
	}

	/**
	 * 获取多个设备的历史数据
	 * 
	 * @param deviceIds 多个设备的id, 以空格分开
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@GetMapping("/devs/{idsAndTimes}")
	public Callable<Object[][]> getValueHistoryArray(@PathVariable String idsAndTimes) throws ParseException {
		Callable<Object[][]> dates = new Callable<Object[][]>() {
            @Override
            public Object[][] call() throws Exception {
            	Object[][] values = null;
        		String[] texts = idsAndTimes.split(",");
        		if (texts.length < 4) {
        			return values;
        		}
        		String deviceIds = texts[0];
        		String startTimeStr = texts[1];
        		String endTimeStr = texts[2];
        		String intervalStr = texts[3];
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        		Date startDate = sdf.parse(startTimeStr);

        		Date endDate = sdf.parse(endTimeStr);
        		// 分钟
        		int interval = Integer.parseInt(intervalStr) * 60;

        		deviceIds = deviceIds.trim();
        		String[] ids = deviceIds.split(" ");
        		for (int i = 0; i < ids.length; i++) {
        			long id = Long.parseLong(ids[i]);
        			List<DeviceValueHistory> list = new ArrayList<>();
        			list = deviceValueHistoryService.findByStartTimeAndEndTimeAndDeviceIdInterval(id, startDate, endDate, interval);

        			if (null == list || list.isEmpty()) {
        				return values;
        			}
//        			Collections.sort(list);
        			String devName = list.get(0).getDeviceName();
        			// 初始化二维数组, 在for循环中只初始化一次
        			if (null == values) {
        				values = new Object[ids.length + 1][list.size() + 1];
        				values[0][0] = "时间";

        				// 设置第0行为时间
        				for (int j = 0; j < list.size(); j++) {
        					DeviceValueHistory history = list.get(j);
        					int clomn = j + 1;
        					values[0][clomn] = history.timeStr();
        				}
        			}
        			// 设置每一行(除第0行)的第0列未设备名
        			values[i + 1][0] = devName;

        			// 设置数值
        			for (int j = 0; j < list.size(); j++) {
        				DeviceValueHistory history = list.get(j);
        				int clomn = j + 1;
        				try {
        					if (clomn < values[i + 1].length) {
        						values[i + 1][clomn] = history.getValue();
        					}
        				} catch (Exception e) {
        					e.printStackTrace();
        				}
        			}
        		}
        		return values;
            }
		};
		return dates;
	}
}
