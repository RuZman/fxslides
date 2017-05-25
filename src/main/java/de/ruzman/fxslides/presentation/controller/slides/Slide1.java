package de.ruzman.fxslides.presentation.controller.slides;

import java.util.ArrayList;
import java.util.List;

import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.gesture.GestureProvider;
import de.ruzman.hui.skeleton.Skeleton;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;

@FXMLController("../../fxml/slides/slide1.fxml")
public class Slide1 implements SkeletonListener {
	@FXML
	public void initialize() {
		SkeletonApp.addListener(this);
		SkeletonApp.addGestureProvider(new GestureProvider() {

			@Override
			public List<String> provideGesture(Skeleton skeleton) {
				List<String> gestures = new ArrayList<>();
				
				if(skeleton == null) {
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

	@Override
	public void onGesture(SkeletonEvent event) {
		
		if (event.getGestures().contains("<1000")) {
			System.out.println("ooo");
		} else if (event.getGestures().contains(">=1000")) {
			System.err.println("xxx");
		}
	}
}
