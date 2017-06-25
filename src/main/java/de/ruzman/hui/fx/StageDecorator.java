package de.ruzman.hui.fx;

import java.util.HashMap;
import java.util.Map;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;

import de.ruzman.common.Point;
import de.ruzman.hui.OnUpdate;
import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.skeleton.Hand;
import de.ruzman.newfx.control.CursorPane.CursorPaneConfiguration;
import de.ruzman.newfx.control.FXCursor.FXCursorConfiguration;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;

public class StageDecorator {
	private ObjectProperty<CursorPaneConfiguration<StageDecoratorConfiguration>> cursorPaneConfiguration = new SimpleObjectProperty<>();
	private Map<Integer, FXCursorConfiguration> pointers = new HashMap<>();

	private StageDecorator() {
		SkeletonApp.addListener(this);
	}

	@OnUpdate
	public void onUpdate(SkeletonEvent event) {
		for (Hand hand : event.getSkeleton().getHands()) {
			if (hand.hasLeft()) {
				onLeaving(hand);
			}

			if (!hand.getPalmPosition().isPresent()) {
				continue;
			}
			Point palmPosition = hand.getPalmPosition().get();

			if (hand.hasEntered()) {
				onEnter(hand, palmPosition);
			} else {
				pointers.get(hand.getId())
						.move(palmPosition.getScreenPosition().getX(), palmPosition.getScreenPosition().getY()).save();
			}
		}
	}
	
	private void onLeaving(Hand hand) {
		cursorPaneConfiguration.get().fxCursors().remove("HAND " + hand.getId()).save();

		if (pointers.size() == 1) {
			restoreMouseOnFXCursorPosition(
					pointers.get(hand.getId()).instance().nodeProperty().get().getTranslateX(),
					pointers.get(hand.getId()).instance().nodeProperty().get().getTranslateY());
		}
		pointers.remove(hand.getId());
	}
	
	private void onEnter(Hand hand, Point palmPosition) {
		if (pointers.containsKey(hand.getId())) {
			return;
		}

		if (pointers.isEmpty()) {
			cursorPaneConfiguration.get().instance().getScene().setCursor(Cursor.NONE);
		}

		System.out.println("Should create");
		//@formatter:off
		cursorPaneConfiguration.get()
			.fxCursors()
				.createOrUpdate("HAND " + hand.getId())
				.move(palmPosition.getScreenPosition().getX(), palmPosition.getScreenPosition().getY())
			.save()
		.save();
		//@formatter:on

		// FIXME: Wozu ist dieser Code gut?
		/*
		 * cursorPaneConfiguration.fxCursor("HAND " +
		 * hand.getId()).instance().nodeProperty().addEventHandler(ANY,
		 * new EventHandler<CursorEvent>() {
		 * 
		 * @Override public void handle(CursorEvent event) {
		 * event.getTragetNode().fireEvent(event); }
		 * 
		 * });
		 */

		pointers.put(hand.getId(), cursorPaneConfiguration.get().fxCursor("HAND " + hand.getId()));
	}

	private void restoreMouseOnFXCursorPosition(Double x, Double y) {
		Robot robot = Application.GetApplication().createRobot();
		robot.mouseMove(x.intValue(), y.intValue());
		cursorPaneConfiguration.get().instance().getScene().setCursor(Cursor.DEFAULT);
	}
	
	public ReadOnlyObjectProperty<CursorPaneConfiguration<StageDecoratorConfiguration>> cursorPaneConfigurationProperty() {
		return cursorPaneConfiguration;
	}

	public static class StageDecoratorConfiguration {
		private StageDecorator stageDecorator;
		private CursorPaneConfiguration<StageDecoratorConfiguration> cursorPaneConfiguration;

		public CursorPaneConfiguration<StageDecoratorConfiguration> cursorPaneConfiguration() {
			if (cursorPaneConfiguration == null) {
				cursorPaneConfiguration = new CursorPaneConfiguration<StageDecoratorConfiguration>(this);
			}
			return cursorPaneConfiguration;
		}
		
		public StageDecorator instance() {
			return stageDecorator;
		}

		public StageDecoratorConfiguration save() {
			if (stageDecorator == null) {
				System.out.println("Init StageDecorator");
				stageDecorator = new StageDecorator();
			}
			
			stageDecorator.cursorPaneConfiguration.set(cursorPaneConfiguration);

			return this;
		}
	}
}
