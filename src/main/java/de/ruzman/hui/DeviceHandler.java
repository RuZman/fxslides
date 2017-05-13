package de.ruzman.hui;

import java.util.LinkedList;
import java.util.List;

public class DeviceHandler {
	private List<Device> devices = new LinkedList<>();
	private List<DeviceListener> deviceListeners = new LinkedList<>();

	public void addDevice(Device device) {
		if (devices.add(device)) {
			device.addDeviceHandler(this);
			device.setStatus(Device.DEVICE_ADDED);
		}
	}

	public void removeDevice(Device device) {
		device.setStatus(Device.DEVICE_REMOVED);
		device.removeDeviceHandler(this);
		devices.remove(device);
	}

	public void addDeviceListener(DeviceListener deviceListener) {
		deviceListeners.add(deviceListener);
	}

	void fireEvent(DeviceEvent deviceEvent) {
		if (devices.contains(deviceEvent.getDevice())) {
			deviceListeners.forEach(e -> e.onStatusChanged(deviceEvent));
		}
	}
}
