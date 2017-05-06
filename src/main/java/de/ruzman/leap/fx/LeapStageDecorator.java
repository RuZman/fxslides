package de.ruzman.leap.fx;

import static de.ruzman.newfx.event.CursorEvent.ANY;
import static de.ruzman.newfx.event.CursorEvent.CURSOR_ENTERED;
import static de.ruzman.newfx.event.CursorEvent.CURSOR_LEFT;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import de.ruzman.newfx.control.CursorNodeFactory;
import de.ruzman.newfx.control.CursorPane;
import de.ruzman.newfx.control.DelayProgress;
import de.ruzman.newfx.event.CursorEvent;

public class LeapStageDecorator {
	private Stage stage;

	public LeapStageDecorator(Stage stage) {
		this.stage = stage;

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
		CursorPane cursorPane = new CursorPane();
		LeapFXCursor<StackPane> cursor = new LeapFXCursor<>();
		cursor.setAdjustX(-8);
		cursor.setAdjustY(-8);
		cursor.setCursorNodeFactory(new CursorNodeFactory<StackPane>() {

			@Override
			public StackPane createCursor() {
				Circle circle = new Circle(0, 0, 18, Color.rgb(240, 240, 240));
				circle.setTranslateY(20);

				Circle circle2 = new Circle(0, 0, 20, Color.rgb(60, 225, 255));
				circle2.setTranslateY(20);

				Path path = new Path();
				path.getElements().add(new MoveTo(0, 0));
				path.getElements().add(new LineTo(0, 40));
				path.getElements().add(new LineTo(35, 28));
				path.getElements().add(new ClosePath());
				path.setFill(Color.rgb(60, 225, 255));
				path.setStrokeWidth(0);
				path.setTranslateX(-2);

				Circle circle3 = new Circle(0, 0, 26, Color.TRANSPARENT);
				circle3.setTranslateY(20);
				circle3.setVisible(false);
				
				return new StackPane(circle3, path, circle2, circle);
			}
		});

		DelayProgress progress = new DelayProgress(250, 0, 0, 130, 26, Color.rgb(60, 225, 255));
		progress.setTranslateY(46);
		progress.setTranslateX(26);
		cursor.getNode().getChildren().add(0, progress);
		// cursor.getNode().getChildren().add(0, circle);
		progress.setVisible(false);
		cursor.getNode().addEventHandler(ANY, new EventHandler<CursorEvent>() {
			@Override
			public void handle(CursorEvent event) {
				event.getTragetNode().fireEvent(event);
				if (event.getEventType().equals(CURSOR_ENTERED)) {
					if (event.getTragetNode() instanceof ButtonBase) {
						ButtonBase buttonBase = (ButtonBase) event.getTragetNode();

						progress.setVisible(true);
						progress.start(new Runnable() {
							@Override
							public void run() {
								buttonBase.fire();
							}
						});
					}
				}
				if (event.getEventType().equals(CURSOR_LEFT)) {
					if (event.getTragetNode() instanceof ButtonBase) {
						progress.setVisible(false);
						progress.reset();
					}
				}
			}

		});

		cursorPane.setCursor(cursor);
		cursor.setVisible(false);
		stage.getScene().setRoot(new StackPane(root, cursorPane));
	}
}
