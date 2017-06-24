package de.ruzman.fxslides.presentation;

import static java.util.Locale.GERMAN;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import de.ruzman.common.Point;
import de.ruzman.fxslides.presentation.controller.slides.Slide0;
import de.ruzman.fxslides.presentation.controller.slides.Slide1;
import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.skeleton.Hand;
import io.datafx.controller.FXMLController;
import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.flow.FlowException;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

@FXMLController("fxml/presentation.fxml")
public class PresentationController implements SkeletonListener {
	@FXML private StackPane presentation;
	
	private StackPane slide0;
	private StackPane slide1;
	
	@PostConstruct
	public void init() {
		SkeletonApp.addListener(this);
		
		String baseName = getClass().getPackage().getName() + ".labels.labels";

		ViewConfiguration viewConfiguration = new ViewConfiguration();
		viewConfiguration.setResources(ResourceBundle.getBundle(baseName, GERMAN));
		
		FlowFixed flowSlide0 = new FlowFixed(Slide0.class, viewConfiguration);
		FlowFixed flowSlide1 = new FlowFixed(Slide1.class, viewConfiguration);
		
		try {
			slide0 = flowSlide0.start();
			slide1 = flowSlide1.start();
			
			presentation.getChildren().add(slide0);
			presentation.getChildren().add(slide1);
			
			slide1.translateXProperty().bind(slide0.widthProperty().add(slide0.translateXProperty()));
		} catch (FlowException e) {
			e.printStackTrace();
		}
	}

	Integer x = null;
	boolean isFinished;
	
	@Override
	public void onUpdate(SkeletonEvent event) {
		if(isFinished) {
			return;
		}
		
		if(event.getSkeleton().getHands().size() == 1) {
			Hand hand = event.getSkeleton().getHands().get(0);
			
			if(hand.getPalmPosition().isPresent()) {
				Point palmPosition = hand.getPalmPosition().get();
				
				if(palmPosition.getAbsolutePosition().getZ() < 0) {
					updateDrag((int) palmPosition.getScreenPosition().getX());
				} else if(x != null) {
					drop();
				}
			} else {
				drop();
			}
		} else {
			drop();
		}
		
		
		
		//slide0.setTranslateX(-1000);
	}
	
	int startValue = 0;

	private void updateDrag(int value) {
		if(x == null) {
			startValue = value - (int) slide0.getTranslateX();
			x = 0;
			// init
		} else if((startValue-value)*1.3 >= slide0.getWidth()) {
			isFinished = true;
			presentation.getChildren().remove(slide0);
			slide1.translateXProperty().unbind();
			slide1.setTranslateX(0);
			return;
		}
		slide0.setTranslateX(-(startValue-value)*1.3);
	}
	
	private void drop() {
		x = null;
	}

	@Override
	public void onGesture(SkeletonEvent event) {
	}
}
