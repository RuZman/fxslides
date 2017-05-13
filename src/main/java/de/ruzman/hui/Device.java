package de.ruzman.hui;

import java.util.LinkedList;
import java.util.List;

public abstract class Device {
	public static final int DEVICE_REMOVED = generateStatusId();
	public static final int DEVICE_NOT_INITIALIZED = generateStatusId();
	public static final int DEVICE_ADDED = generateStatusId();
	
	private int status = DEVICE_NOT_INITIALIZED;
	
	private List<DeviceHandler> deviceHandlers = new LinkedList<>();
	
	public abstract void provide(Human human);

	public void setStatus(int status) {
		this.status = status;
		fireStatusChanged();
	}
	
	public int getStatus() {
		return status;
	}
	
	void fireStatusChanged() {
		deviceHandlers.forEach(e -> e.fireEvent(new DeviceEvent(this)));
	}
	
	void addDeviceHandler(DeviceHandler deviceHandler) {
		deviceHandlers.add(deviceHandler);
	}
	
	void removeDeviceHandler(DeviceHandler deviceHandler) {
		deviceHandlers.remove(deviceHandler);
	}
	
	private static int statusId = 0;
	public final static synchronized int generateStatusId() {
		return statusId++;
	}
}
