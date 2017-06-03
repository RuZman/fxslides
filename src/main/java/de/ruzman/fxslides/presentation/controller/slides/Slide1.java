package de.ruzman.fxslides.presentation.controller.slides;

import java.util.ArrayList;
import java.util.List;

import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.gesture.GestureProvider;
import de.ruzman.hui.skeleton.Skeleton;
import de.ruzman.leap.LeapApp;
import de.ruzman.newfx.control.CursorNode;
import de.ruzman.newfx.control.CursorNodeFactory;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

@FXMLController("../../fxml/slides/slide1.fxml")
public class Slide1 implements SkeletonListener {
	@FXML
	public void initialize() {
		SkeletonApp.addListener(this);
		SkeletonApp.addGestureProvider(new GestureProvider() {

			@Override
			public List<String> provideGesture(Skeleton skeleton) {
				List<String> gestures = new ArrayList<>();

				if (skeleton == null) {
					return gestures;
				}

				if (!skeleton.getHands().isEmpty() && skeleton.getHands().get(0).getPalmPosition().isPresent()) {
					if (skeleton.getHands().get(0).getPalmPosition().get().getScreenPosition().getX() < 1000) {
						gestures.add("<1000");
					} else {
						gestures.add(">=1000");
					}
				}
				return gestures;
			}
		});
	}

	@Override
	public void onUpdate(SkeletonEvent event) {
		if (event.getSkeleton().hasEntered()) {
			System.out.println("enter");
		} else if (event.getSkeleton().hasLeft()) {
			System.out.println("left");
		} else {
		}
	}

	private boolean isRight = true;

	@Override
	public void onGesture(SkeletonEvent event) {
		CursorNodeFactory cursorNodeFactory = new CursorNodeFactory() {

			@Override
			public Node createCursor() {
				return new Circle(20.0, Color.ROYALBLUE);
			}
		};
		CursorNodeFactory cursorNodeFactory2 = new CursorNodeFactory() {

			@Override
			public Node createCursor() {
				return new Rectangle(20.0, 50.0, Color.RED);
			}
		};

		if (event.getGestures().contains("<1000")) {
			if (isRight) {
				LeapApp.leapStageDecorator().cursorPaneConfigurationProperty().get()
						.fxCursor("HAND " + event.getSkeleton().getHands().get(0).getId()).overwriteCursorNode()
						.adjust(10, 10).cursorNodeFactory(cursorNodeFactory).save()
						.move(event.getSkeleton().getHands().get(0).getPalmPosition().get().getScreenPosition()
								.getX(),
								event.getSkeleton().getHands().get(0).getPalmPosition().get().getScreenPosition()
										.getY())
						.save();
				isRight = false;
			}
		} else if (event.getGestures().contains(">=1000")) {
			if (!isRight) {
				LeapApp.leapStageDecorator().cursorPaneConfigurationProperty().get()
						.fxCursor("HAND " + event.getSkeleton().getHands().get(0).getId()).overwriteCursorNode()
						.adjust(10, 25).cursorNodeFactory(cursorNodeFactory2).save()
						.move(event.getSkeleton().getHands().get(0).getPalmPosition().get().getScreenPosition()
								.getX(),
								event.getSkeleton().getHands().get(0).getPalmPosition().get().getScreenPosition()
										.getY())
						.save();
				isRight = true;
			}
		}
	}
}
