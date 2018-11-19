package com.bairock.eleMonitor.comm;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.Station.OnStateChangedListener;
import com.bairock.eleMonitor.enums.StationState;

public class MyOnStationStateChangedListener implements OnStateChangedListener {

	private WebBroadcast webBroadcast = SpringUtil.getBean(WebBroadcast.class);
	
	@Override
	public void onStateChanged(Station station, StationState state) {
		webBroadcast.broadcastStationStateChanged(station.getId(), station.getName(), state);
	}

}
