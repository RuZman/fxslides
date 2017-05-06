package de.ruzman.leap.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

import de.ruzman.leap.LeapApp;
import de.ruzman.leap.event.LeapEvent.Status;

public final class LeapEventHandler extends Listener {
	private static final LeapEventHandler SINGLETON = new LeapEventHandler();

	private List<LeapListener> leapListeners;
	private LeapEvent leapEvent;
	
	private long lastFrameId = 0;
	private long currentFrameId = 0;
	
	
	private boolean isInitalized;
	private boolean isServiceConnected;
	private boolean isConnected;
	private boolean hasFocus;

	private LeapEvent.Status status;

	private LeapEventHandler() {
		leapListeners = new CopyOnWriteArrayList<>();
	}

	public static LeapEventHandler getInstance() {
		return SINGLETON;
	}

	public static void addLeapListener(LeapListener leapListener) {
		SINGLETON.leapListeners.add(leapListener);
	}

	public static void removeLeapListener(LeapListener leapListener) {
		SINGLETON.leapListeners.remove(leapListener);
	}

	public static void removeAllLeapListener() {
		SINGLETON.leapListeners.clear();
	}

	public static void fireFrameUpdate() {
		SINGLETON.onFrame(LeapApp.getController());
	}

	@Override
	public void onInit(Controller controller) {
		setLeapEvent(Status.ON_INIT);
	}

	@Override
	public void onServiceConnect(Controller controller) {
		setLeapEvent(Status.ON_SERVICE_CONNECT);
	}

	@Override
	public void onConnect(Controller controller) {
		setLeapEvent(Status.ON_CONNECT);
	}

	@Override
	public void onFocusGained(Controller controller) {
		setLeapEvent(Status.ON_FOCUS_GAINED);
	}

	@Override
	public void onFocusLost(Controller controller) {
		setLeapEvent(Status.ON_FOCUS_LOST);
	}

	@Override
	public void onDisconnect(Controller controller) {
		setLeapEvent(Status.ON_DISCONNECT);
	}

	@Override
	public void onServiceDisconnect(Controller controller) {
		setLeapEvent(Status.ON_SERVICE_DISCONNECT);
	}
	
	@Override
	public void onFrame(Controller controller) {
		currentFrameId = controller.frame().id();
		
		if(lastFrameId != currentFrameId) {
			for (LeapListener leapListener : leapListeners) {
				leapListener.onFrame(controller);
			}
		}
		lastFrameId = currentFrameId;
	}

	private void fireStatusChanged() {
		for (LeapListener leapListener : leapListeners) {
			leapListener.statusChanged(leapEvent);
		}
	}

	public static void update() {
		Controller controller = LeapApp.getController();

		if (!SINGLETON.isInitalized) {
			SINGLETON.onInit(controller);
			SINGLETON.isInitalized = true;
		}

		if (controller.isServiceConnected() && !SINGLETON.isServiceConnected) {
			SINGLETON.onServiceConnect(controller);
			SINGLETON.isServiceConnected = true;
		}

		if (controller.isConnected() && !SINGLETON.isConnected) {
			SINGLETON.onConnect(controller);
			SINGLETON.isConnected = true;
		}

		if (controller.hasFocus() && !SINGLETON.hasFocus) {
			SINGLETON.onFocusGained(controller);
			SINGLETON.hasFocus = true;
		}

		if (!controller.hasFocus() && SINGLETON.hasFocus) {
			SINGLETON.onFocusLost(controller);
			SINGLETON.hasFocus = false;
		}

		if (!controller.isConnected() && SINGLETON.isConnected) {
			SINGLETON.onDisconnect(controller);
			SINGLETON.isConnected = false;
		}

		if (!controller.isServiceConnected() && SINGLETON.isServiceConnected) {
			SINGLETON.onServiceDisconnect(controller);
			SINGLETON.isServiceConnected = false;
		}

		// FIXME: Code changed to work?
		if(true) {
			SINGLETON.onFrame(LeapApp.getController());
		}
	}

	private void setLeapEvent(Status status) {
		leapEvent = new LeapEvent(this.status, status);
		this.status = status;
		
		if (!leapEvent.getStatus().equals(leapEvent.getPrevStatus())) {
			fireStatusChanged();
		}
	}
}