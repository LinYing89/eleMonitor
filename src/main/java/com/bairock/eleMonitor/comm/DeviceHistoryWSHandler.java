package com.bairock.eleMonitor.comm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.data.DeviceValueHistory;
import com.bairock.eleMonitor.data.webData.ChartData;
import com.bairock.eleMonitor.data.webData.ChartSeries;
import com.bairock.eleMonitor.service.DeviceValueHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeviceHistoryWSHandler extends TextWebSocketHandler {

	private DeviceValueHistoryService deviceValueHistoryService = SpringUtil.getBean(DeviceValueHistoryService.class);

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
	}

//	@Override
//	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//		
//		String text = message.getPayload();
//		String[] texts = text.split(",");
//		if (texts.length < 3) {
//			ChartData chartData = new ChartData();
//			chartData.setEnded(true);
//			writeChartData(chartData, session);
//			return;
//		}
//		String deviceIds = texts[0];
//		String startTimeStr = texts[1];
//		String endTimeStr = texts[2];
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		Date startDate = sdf.parse(startTimeStr);
//		Date endDate = sdf.parse(endTimeStr);
//
//		deviceIds = deviceIds.trim();
//		String[] ids = deviceIds.split(" ");
//
//		// 每个设备历史纪录下一次循环查询的开始的id
////		long[] startIds = new long[ids.length];
//		Date[] nextStartTime = new Date[ids.length];
//		for (int i = 0; i < nextStartTime.length; i++) {
//			nextStartTime[i] = startDate;
//		}
//
//		int readSize = 5000;
//		Thread t = new Thread() {
//			ChartData chartData = null;
//			Object[][] values = null;
//			boolean ended = false;
//			boolean first = true;
//
//			@Override
//			public void run() {
//				while (!ended && session.isOpen()) {
//					chartData = new ChartData();
//					values = null;
//					for (int i = 0; i < ids.length; i++) {
//						if (!session.isOpen()) {
//							return;
//						}
//						chartData.getListChartSeries().add(new ChartSeries());
//
//						long id = Long.parseLong(ids[i]);
//						// 每次获取1000条
//						List<DeviceValueHistory> list = deviceValueHistoryService
//								.findByStartTimeAndEndTimeAndDeviceIdAndLimit(nextStartTime[i], endDate, id, readSize);
//						if (null == list || list.isEmpty()) {
//							chartData.setEnded(true);
//							writeChartData(chartData, session);
//							return;
//						}
//						// 小于1000条表示所有数据获取完毕
//						if (list.size() < readSize) {
//							ended = true;
//							chartData.setEnded(true);
//						} else {
////							startIds[i] = list.get(list.size() - 1).getId();
//							nextStartTime[i] = list.get(list.size() - 1).getTime();
//						}
//
//						Collections.sort(list);
//						String devName = list.get(0).getDeviceName();
//						// 初始化二维数组, 在for循环中只初始化一次
//						if (null == values) {
//							if (first) {
//								values = new Object[ids.length + 1][list.size() + 1];
//								values[0][0] = "时间";
//							} else {
//								values = new Object[ids.length + 1][list.size()];
//							}
//
//							// 设置第0行为时间
//							for (int j = 0; j < list.size(); j++) {
//								DeviceValueHistory history = list.get(j);
//								int clomn = first ? j + 1 : j;
//								values[0][clomn] = history.timeStr();
//							}
//						}
//						// 设置每一行(除第0行)的第0列未设备名
//						if (first) {
//							values[i + 1][0] = devName;
//						}
//
//						// 设置数值
//						for (int j = 0; j < list.size(); j++) {
//							DeviceValueHistory history = list.get(j);
//							int clomn = first ? j + 1 : j;
//							try {
//								if (clomn < values[i + 1].length) {
//									values[i + 1][clomn] = history.getValue();
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}
//					chartData.setDataSet(values);
//					if (!session.isOpen()) {
//						return;
//					}
//					writeChartData(chartData, session);
//					try {
//						Thread.sleep(20);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					first = false;
//				}
//			}
//		};
//		t.start();
//	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		String text = message.getPayload();
		String[] texts = text.split(",");
		if (texts.length < 3) {
			ChartData chartData = new ChartData();
			chartData.setEnded(true);
			writeChartData(chartData, session);
			return;
		}
		String deviceIds = texts[0];
		String startTimeStr = texts[1];
		String endTimeStr = texts[2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDate = sdf.parse(startTimeStr);
		Date endDate = sdf.parse(endTimeStr);

		deviceIds = deviceIds.trim();
		String[] ids = deviceIds.split(" ");

		// 每个设备历史纪录下一次循环查询的开始的id
//		long[] startIds = new long[ids.length];
		Date[] nextStartTime = new Date[ids.length];
		for (int i = 0; i < nextStartTime.length; i++) {
			nextStartTime[i] = startDate;
		}

		int readSize = 5000;
		Thread t = new Thread() {
			ChartData chartData = null;
			Object[][] values = null;
			boolean ended = false;
			boolean first = true;

			@Override
			public void run() {
				while (!ended && session.isOpen()) {
					chartData = new ChartData();
					values = null;
					for (int i = 0; i < ids.length; i++) {
						if (!session.isOpen()) {
							return;
						}
						chartData.getListChartSeries().add(new ChartSeries());

						long id = Long.parseLong(ids[i]);
						// 每次获取1000条
						List<DeviceValueHistory> list = deviceValueHistoryService
								.findByStartTimeAndEndTimeAndDeviceIdAndLimit(nextStartTime[i], endDate, id, readSize);
						if (null == list || list.isEmpty()) {
							chartData.setEnded(true);
							writeChartData(chartData, session);
							return;
						}
						// 小于1000条表示所有数据获取完毕
						if (list.size() < readSize) {
							ended = true;
							chartData.setEnded(true);
						} else {
//							startIds[i] = list.get(list.size() - 1).getId();
							nextStartTime[i] = list.get(list.size() - 1).getTime();
						}

						Collections.sort(list);
						String devName = list.get(0).getDeviceName();
						// 初始化二维数组, 在for循环中只初始化一次
						if (null == values) {
							if (first) {
								values = new Object[ids.length + 1][list.size() + 1];
								values[0][0] = "时间";
							} else {
								values = new Object[ids.length + 1][list.size()];
							}

							// 设置第0行为时间
							for (int j = 0; j < list.size(); j++) {
								DeviceValueHistory history = list.get(j);
								int clomn = first ? j + 1 : j;
								values[0][clomn] = history.timeStr();
							}
						}
						// 设置每一行(除第0行)的第0列未设备名
						if (first) {
							values[i + 1][0] = devName;
						}

						// 设置数值
						for (int j = 0; j < list.size(); j++) {
							DeviceValueHistory history = list.get(j);
							int clomn = first ? j + 1 : j;
							try {
								if (clomn < values[i + 1].length) {
									values[i + 1][clomn] = history.getValue();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					chartData.setDataSet(values);
					if (!session.isOpen()) {
						return;
					}
					writeChartData(chartData, session);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					first = false;
				}
			}
		};
		t.start();
	}
	
	private void writeChartData(ChartData chartData, WebSocketSession session) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String msg = mapper.writeValueAsString(chartData);
			session.sendMessage(new TextMessage(msg));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
