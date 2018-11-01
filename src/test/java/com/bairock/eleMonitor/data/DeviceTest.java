package com.bairock.eleMonitor.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DeviceTest {

	Device device;
	
	@Before
	public void setUp() throws Exception {
		device = new Device();
		device.setByteOrder(12);
	}

	@Test
	public void testHandler() {
		byte[] by = new byte[] {0x0f, 9, 2};
		device.handler(by);
		assertEquals(985346, device.getValue(), 0.01);
		device.setByteOrder(21);
		device.handler(by);
		assertEquals(133391, device.getValue(), 0.01);
	}

}
