package com.bairock.eleMonitor.controller;

import java.util.Collections;
import java.util.List;

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
import com.bairock.eleMonitor.data.webData.ChartData;
import com.bairock.eleMonitor.data.webData.ChartSeries;
import com.bairock.eleMonitor.service.DeviceValueHistoryService;
import com.bairock.eleMonitor.service.SubstationService;

@Controller
@RequestMapping("/valueHistory")
public class DeviceValueHistoryController {

	@Autowired
	private DeviceValueHistoryService deviceValueHistoryController;
	@Autowired
	private SubstationService substationService;

	@GetMapping("/{substationId}/{deviceId}")
	public String getHistory(@PathVariable long substationId, @PathVariable long deviceId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		List<Device> listValueDevice = substation.findAllValueDevice();
		List<Device> listCtrlDevice = substation.findAllCtrlDevice();
		for (Device dev : listValueDevice) {
			List<DeviceValueHistory> list = deviceValueHistoryController.findAllByDeviceId(dev.getId());
			dev.setListValueHistory(list);
		}
		for (Device dev : listCtrlDevice) {
			List<DeviceValueHistory> list = deviceValueHistoryController.findAllByDeviceId(dev.getId());
			dev.setListValueHistory(list);
		}
		model.addAttribute("substation", substation);
		model.addAttribute("listCtrlDevice", listCtrlDevice);
		model.addAttribute("listValueDevice", listValueDevice);
		Device mainDevice = null;
		if(!listValueDevice.isEmpty()) {
			mainDevice = listValueDevice.get(0);
		}else if(listCtrlDevice.isEmpty()) {
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
		List<DeviceValueHistory> list = deviceValueHistoryController.findAllByDeviceId(deviceId);
		if(null == list || list.isEmpty()) {
			return new Object[0][0];
		}
		Collections.sort(list);
		Object[][] values = new Object[3][list.size() + 1];
		values[0][0] = "device";
		values[1][0] = list.get(0).getDevice().getName();
		values[2][0] = list.get(0).getDevice().getName() + "2";
		for(int i=0; i<list.size(); i++) {
			DeviceValueHistory history = list.get(i);
			values[0][i + 1] = history.timeStr();
			values[1][i + 1] = history.getValue();
			values[2][i + 1] = history.getValue() + 20;
		}
		return values;
	}
	
	/**
	 * 获取多个设备的历史数据
	 * @param deviceIds 多个设备的id, 以空格分开
	 * @return
	 */
	@ResponseBody
	@GetMapping("/devs/{deviceIds}")
	public ChartData getValueHistoryArray(@PathVariable String deviceIds) {
		ChartData chartData = new ChartData();
		deviceIds = deviceIds.trim();
		String[] ids = deviceIds.split(" ");
		Object[][] values = null;
		for(int i=0; i<ids.length; i++) {
			
			chartData.getListChartSeries().add(new ChartSeries());
			
			long id = Long.parseLong(ids[i]);
			List<DeviceValueHistory> list = deviceValueHistoryController.findAllByDeviceId(id);
			if(null == list || list.isEmpty()) {
				continue;
			}
			Collections.sort(list);
			String devName = list.get(0).getDevice().getName();
			//初始化二维数组, 在for循环中只初始化一次
			if(null == values) {
				values = new Object[ids.length + 1][list.size() + 1];
				values[0][0] = "device";
			}
			//设置每一行(除第0行)的第0列未设备名
			values[i + 1][0] = devName;
			//设置第0行为时间
			for(int j=0; j<list.size(); j++) {
				DeviceValueHistory history = list.get(j);
				values[0][j + 1] = history.timeStr();
			}
			//设置数值
			for(int j=0; j<list.size(); j++) {
				DeviceValueHistory history = list.get(j);
				values[i + 1][j + 1] = history.getValue();
			}
		}
		chartData.setDataSet(values);
		
		return chartData;
	}
}
