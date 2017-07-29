package de.ruzman.fxslides.presentation.controller.slides;

import javax.annotation.PostConstruct;

import com.leapmotion.leap.Vector;

import de.ruzman.fxslides.presentation.SimplePalmDragAndDropGestureProvider;
import de.ruzman.hui.OnUpdate;
import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.skeleton.FingerType;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

@FXMLController("../../fxml/slides/slide1.fxml")
public class Slide1 {
	@FXML private ImageView minorityReport;
	@FXML @ActionTrigger("test")private Button testButton;
	
	@PostConstruct
	public void init() {
		minorityReport.sceneProperty().addListener((a, b, c) -> {
			if(c == null) {
				return;
			}
			
			minorityReport.fitWidthProperty().bind(c.widthProperty());
		});
	}
	
	private int startValue = 0;
	private int startValueY = 0;
	private boolean scaleOff = false;
	
	@OnUpdate
	public void onUpdate(SkeletonEvent event) {
		if(event.getSkeleton().getHands().size() == 2) {
			SkeletonApp.removeListener(this);
			SkeletonApp.removeGestureProvider(new SimplePalmDragAndDropGestureProvider("Slide1"));
			testButton.fire();
			return;
		}
		
		if(event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition() == null) {
			return;
		}
		
		Vector position = event.getSkeleton().getHands().get(0).getWristPosition().get().getScreenPosition();
		
		if(event.getGestures().contains("Slide1dragStart")) {
			startValue = (int) position.getX();
			startValueY = (int) position.getY();
		}
		
		if(event.getGestures().contains("Slide1drag")) {
			double xDistance = event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().yxDistanceTo(
					event.getSkeleton().getHands().get(0).getFinger(FingerType.INDEX).getTipPosition());
			
			if(xDistance > 50 && !scaleOff) {
				minorityReport.setFitWidth(minorityReport.getScene().getWidth());
			} else if(xDistance > 25 && !scaleOff) {
				System.out.println(xDistance);
				startValue = (int) position.getX();
				startValueY = (int) position.getY();
				minorityReport.setFitWidth(1920/50*(xDistance));
			} else {
				scaleOff = true;
				minorityReport.setFitWidth(minorityReport.getScene().getWidth()/50*25);
				minorityReport.setTranslateX(-(startValue - (int) position.getX()));
				minorityReport.setTranslateY(-(startValueY - (int) position.getY()));
			}
		}
	}
	
	public void activate() {
		SkeletonApp.addListener(this);
		SkeletonApp.addGestureProvider(new SimplePalmDragAndDropGestureProvider("Slide1"));
		minorityReport.fitWidthProperty().unbind();
	}
}
