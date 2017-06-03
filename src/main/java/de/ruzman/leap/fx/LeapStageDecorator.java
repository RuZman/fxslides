package de.ruzman.leap.fx;

import java.util.HashMap;
import java.util.Map;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;

import de.ruzman.common.Point;
import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.skeleton.Hand;
import de.ruzman.newfx.control.CursorNodeFactory;
import de.ruzman.newfx.control.CursorPane.CursorPaneConfiguration;
import de.ruzman.newfx.control.FXCursor.FXCursorConfiguration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LeapStageDecorator implements SkeletonListener {
	private Stage stage;
	private CursorPaneConfiguration cursorPaneConfiguration;
	private Map<Integer, FXCursorConfiguration> pointers;
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
	
	public CursorPaneConfiguration getCursorPaneConfiguration() {
		return cursorPaneConfiguration;
	}

	private void decorateStage(Node root) {
		cursorPaneConfiguration = new CursorPaneConfiguration().save();
		stage.getScene().setRoot(new StackPane(root, cursorPaneConfiguration.instance()));
	}

	private void restoreMouseOnFXCursorPosition(Double x, Double y) {
		Robot robot = Application.GetApplication().createRobot();
		robot.mouseMove(x.intValue(), y.intValue());
		cursorPaneConfiguration.instance().getScene().setCursor(Cursor.DEFAULT);
	}

	@Override
	public void onUpdate(SkeletonEvent event) {
		for (Hand hand : event.getSkeleton().getHands()) {
			if (hand.hasLeft()) {
				cursorPaneConfiguration.fxCursors().remove("HAND " + hand.getId()).save();
				
				if (pointers.size() == 1) {
					restoreMouseOnFXCursorPosition(
							pointers.get(hand.getId()).instance().nodeProperty().get().getTranslateX(),
							pointers.get(hand.getId()).instance().nodeProperty().get().getTranslateY());
				}
				pointers.remove(hand.getId());
			}

			if (!hand.getPalmPosition().isPresent()) {
				continue;
			}
			Point palmPosition = hand.getPalmPosition().get();

			if (hand.hasEntered()) {
				if (pointers.containsKey(hand.getId())) {
					return;
				}

				if (pointers.isEmpty()) {
					cursorPaneConfiguration.instance().getScene().setCursor(Cursor.NONE);
				}

				cursorPaneConfiguration
					.defaultCursorNode()
						.adjust(-8, -8)
						.cursorNodeFactory(cursorNodeFactory)
						.save()
					.fxCursors()
						.createOrUpdate("HAND " + hand.getId())
						.move(palmPosition.getScreenPosition().getX(), palmPosition.getScreenPosition().getY())
						.save()
					.save();
				
				// FIXME: Events?
				/*cursorPaneConfiguration.fxCursor("HAND " + hand.getId()).instance().nodeProperty().addEventHandler(ANY, new EventHandler<CursorEvent>() {
					@Override
					public void handle(CursorEvent event) {
						event.getTragetNode().fireEvent(event);
					}

				});*/

				pointers.put(hand.getId(), cursorPaneConfiguration.fxCursor("HAND " + hand.getId()));

			} else {
				pointers.get(hand.getId()).move(palmPosition.getScreenPosition().getX(),
						palmPosition.getScreenPosition().getY()).save();
			}
		}
	}

	@Override
	public void onGesture(SkeletonEvent event) {
	}
}
