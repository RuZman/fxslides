package de.ruzman.leap.event;

import com.leapmotion.leap.Controller;

public interface LeapListener {	
	public void onFrame(Controller controller);
	public void statusChanged(LeapEvent event);
}