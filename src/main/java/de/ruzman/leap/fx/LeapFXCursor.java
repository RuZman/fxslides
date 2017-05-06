package de.ruzman.leap.fx;

import javafx.scene.Cursor;
import javafx.scene.Node;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;

import de.ruzman.leap.LeapApp;
import de.ruzman.leap.event.PointEvent;
import de.ruzman.leap.event.PointMotionListener;
import de.ruzman.newfx.control.FXCursor;

public class LeapFXCursor<T extends Node> extends FXCursor<T> implements PointMotionListener {
	private long handId = -1;

	public LeapFXCursor() {
		LeapApp.getMotionRegistry().addListener(this);
		setVisible(false);
	}

	@Override
	public void enteredViewoport(PointEvent event) {
		if (handId == -1) {
			handId = event.getSource().id();
			getNode().getScene().setCursor(Cursor.NONE);
		}
		setVisible(true);
	}

	@Override
	public void moved(PointEvent event) {
		if (event.getSource().id() == handId) {
			move(event.getX(), event.getY());
		}
	}

	@Override
	public void leftViewport(PointEvent event) {
		if (event.getSource().id() == handId) {
			handId = -1;
			setVisible(false);
			restoreMouseOnFXCursorPosition();
		}
	}

	private void restoreMouseOnFXCursorPosition() {
		Robot robot = Application.GetApplication().createRobot();
		robot.mouseMove(x.intValue(), y.intValue());
		getNode().getScene().setCursor(Cursor.DEFAULT);
	}
}
