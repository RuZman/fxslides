package de.ruzman.leap.fx;

import static de.ruzman.newfx.event.CursorEvent.ANY;

import java.util.HashMap;
import java.util.Map;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;

import de.ruzman.leap.LeapApp;
import de.ruzman.leap.event.PointEvent;
import de.ruzman.leap.event.PointMotionListener;
import de.ruzman.newfx.control.CursorNodeFactory;
import de.ruzman.newfx.control.CursorPane;
import de.ruzman.newfx.control.FXCursor;
import de.ruzman.newfx.event.CursorEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class LeapStageDecorator implements PointMotionListener {
	private Stage stage;
	private CursorPane cursorPane;
	private Map<Integer, FXCursor<?>> pointers;
	
	// Test

	public LeapStageDecorator(Stage stage) {
		LeapApp.getMotionRegistry().addListener(this);
		this.stage = stage;
		this.pointers = new HashMap<>();

		stage.sceneProperty().addListener(new ChangeListener<Scene>() {
			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
				decorateStage(newValue.getRoot());

				newValue.getRoot().getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
					@Override
					public void onChanged(Change<? extends Node> c) {
						c.next();
						if (c.wasAdded()) {
							decorateStage(c.getAddedSubList().get(0));
						}
					}
				});
			}
		});
	}

	private void decorateStage(Node root) {
		cursorPane = new CursorPane();
		stage.getScene().setRoot(new StackPane(root, cursorPane));
	}

	@Override
	public void enteredViewoport(PointEvent event) {		
		if(pointers.containsKey(event.getSource().id())) {
			return;
		}
		
		if(pointers.isEmpty()) {
			cursorPane.getScene().setCursor(Cursor.NONE);
		}
		
		FXCursor<Circle> cursor = new FXCursor<>();
		cursor.setAdjustX(-8);
		cursor.setAdjustY(-8);
		cursor.setCursorNodeFactory(new CursorNodeFactory<Circle>() {

			@Override
			public Circle createCursor() {
				return new Circle(0, 0, 18, Color.rgb(240, 240, 240));
			}
		});
		
		cursor.getNode().addEventHandler(ANY, new EventHandler<CursorEvent>() {
			@Override
			public void handle(CursorEvent event) {
				event.getTragetNode().fireEvent(event);
			}

		});
		
		cursorPane.addCursor(cursor);
		cursor.setVisible(true);
		pointers.put(event.getSource().id(), cursor);
	}

	@Override
	public void moved(PointEvent event) {
		pointers.get(event.getSource().id()).move(event.getX(), event.getY());
	}

	@Override
	public void leftViewport(PointEvent event) {
		// FIXME: Should remove the pointer from cursorPane and not only set is invisible.
		pointers.get(event.getSource().id()).setVisible(false);;
		pointers.remove(event.getSource().id());
		
		if(pointers.size() == 0) {
			restoreMouseOnFXCursorPosition(event.getX(), event.getY());
		}
	}
	
	private void restoreMouseOnFXCursorPosition(Float x, Float y) {
		Robot robot = Application.GetApplication().createRobot();
		robot.mouseMove(x.intValue(), y.intValue());
		cursorPane.getScene().setCursor(Cursor.DEFAULT);
	}
}
