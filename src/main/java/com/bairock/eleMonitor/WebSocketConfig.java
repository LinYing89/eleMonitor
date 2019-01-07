package com.bairock.eleMonitor;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.bairock.eleMonitor.comm.DeviceHistoryWSHandler;

//@Configuration
//@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(deviceHistoryWSHandler(), "/devHistory");
	}
	
	@Bean
	public DeviceHistoryWSHandler deviceHistoryWSHandler() {
		return new DeviceHistoryWSHandler();
	}

}
