package de.ruzman.leap.event;

public final class LeapEvent {
	public enum Status {
		ON_INIT, ON_SERVICE_CONNECT, ON_CONNECT, ON_FOCUS_GAINED, ON_FOCUS_LOST, ON_DISCONNECT, ON_SERVICE_DISCONNECT;
	}

	private Status prevStatus;
	private Status status;

	public LeapEvent(Status prevStatus, Status status) {
		this.prevStatus = prevStatus;
		this.status = status;
	}

	public boolean onInit() {
		return status.equals(Status.ON_INIT);
	}
	
	public boolean onServiceConnect() {
		return status.equals(Status.ON_SERVICE_CONNECT);
	}
	
	public boolean onConnect() {
		return status.equals(Status.ON_CONNECT);
	}
	
	public boolean onFocusGained() {
		return status.equals(Status.ON_FOCUS_GAINED);
	}
	
	public boolean onFocusLost() {
		return status.equals(Status.ON_FOCUS_LOST);
	}
	
	public boolean onDisconnect() {
		return status.equals(Status.ON_DISCONNECT);
	}
	
	public boolean onServiceDisconnect() {
		return status.equals(Status.ON_SERVICE_DISCONNECT);
	}

	public Status getPrevStatus() {
		return prevStatus;
	}

	public Status getStatus() {
		return status;
	}
}
