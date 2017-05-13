package de.ruzman.leap.hui.leap;

import com.leapmotion.leap.Controller;

import de.ruzman.hui.Device;
import de.ruzman.hui.Human;
import de.ruzman.leap.NativeLibrary;

public class LeapMotion extends Device {
	Controller controller = new Controller();
	
	@Override
	public void provide(Human human) {
		controller.frame().hands().count();
	}
}
