package de.ruzman.fxslides.presentation;

import de.ruzman.hui.DeviceEvent;
import de.ruzman.hui.DeviceHandler;
import de.ruzman.hui.DeviceListener;
import de.ruzman.leap.NativeLibrary;
import de.ruzman.leap.hui.leap.LeapMotion;

public class Main implements DeviceListener {
	public static void main(String[] args) {
		NativeLibrary.loadSystem("native");
		new Main();
	}
	
	public Main() {
		LeapMotion leapMotion = new LeapMotion();
		DeviceHandler deviceHandler = new DeviceHandler();
		deviceHandler.addDeviceListener(this);
		deviceHandler.addDevice(leapMotion);
		deviceHandler.removeDevice(leapMotion);
	}

	@Override
	public void onStatusChanged(DeviceEvent deviceEvent) {
		System.out.println("A:" + deviceEvent.getDevice().getStatus());
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null;
	}
	
	@Override
	public int hashCode() {
		return 5;
	}
}
