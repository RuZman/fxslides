package de.ruzman.leap.fx;

import static de.ruzman.newfx.event.CursorEvent.ANY;

import java.util.HashMap;
import java.util.Map;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;

import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.skeleton.Hand;
import de.ruzman.hui.skeleton.Point;
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
import javafx.stage.Stage;

public class LeapStageDecorator implements SkeletonListener {
	private Stage stage;
	private CursorPane cursorPane;
	private Map<Integer, FXCursor> pointers;
	private CursorNodeFactory cursorNodeFactory;

	public LeapStageDecorator(Stage stage, CursorNodeFactory cursorNodeFactory) {
		SkeletonApp.addListener(this);
		this.stage = stage;
		this.pointers = new HashMap<>();
		this.cursorNodeFactory = cursorNodeFactory;

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

	public void setCursorNodeFactory(CursorNodeFactory cursorNodeFactory) {
		this.cursorNodeFactory = cursorNodeFactory;
	}
	
	private void restoreMouseOnFXCursorPosition(Double x, Double y) {
		Robot robot = Application.GetApplication().createRobot();
		robot.mouseMove(x.intValue(), y.intValue());
		cursorPane.getScene().setCursor(Cursor.DEFAULT);
	}

	@Override
	public void onUpdate(SkeletonEvent event) {
		for(Hand hand: event.getSkeleton().getHands()) {
			if(hand.hasLeft()) {
				// FIXME: Should remove the pointer from cursorPane and not only set is invisible.
				pointers.get(hand.getId()).setVisible(false);
				
				if(pointers.size() == 1) {
					restoreMouseOnFXCursorPosition(pointers.get(hand.getId()).getNode().getTranslateX(), pointers.get(hand.getId()).getNode().getTranslateY());
				}
				pointers.remove(hand.getId());
			} 
			
			if(!hand.getPalmPosition().isPresent()) {
				continue;
			}			
			Point palmPosition = hand.getPalmPosition().get();
			
			if(hand.hasEntered()) {
				if(pointers.containsKey(hand.getId())) {
					return;
				}
				
				if(pointers.isEmpty()) {
					cursorPane.getScene().setCursor(Cursor.NONE);
				}
				
				FXCursor cursor = new FXCursor();
				cursor.setAdjustX(-8);
				cursor.setAdjustY(-8);
				cursor.setCursorNodeFactory(cursorNodeFactory);
				
				cursor.getNode().addEventHandler(ANY, new EventHandler<CursorEvent>() {
					@Override
					public void handle(CursorEvent event) {
						event.getTragetNode().fireEvent(event);
					}

				});
				
				cursorPane.addCursor(cursor);
				pointers.put(hand.getId(), cursor);
				pointers.get(hand.getId()).move(palmPosition.getScreenPosition().getX(), palmPosition.getScreenPosition().getY());
				cursor.setVisible(true);
	
			} else {
				pointers.get(hand.getId()).move(palmPosition.getScreenPosition().getX(), palmPosition.getScreenPosition().getY());
			}
		}
	}

	@Override
	public void onGesture(SkeletonEvent event) {
	}
}
