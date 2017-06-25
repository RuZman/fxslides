package de.ruzman.fxslides.presentation.controller.slides;

import javax.annotation.PostConstruct;

import de.ruzman.fxslides.presentation.SimplePalmDragAndDropGestureProvider;
import de.ruzman.hui.OnUpdate;
import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.skeleton.FingerType;
import de.ruzman.leap.LeapApp;
import de.ruzman.newfx.control.CursorNodeFactory;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

@FXMLController("../../fxml/slides/slide1.fxml")
public class Slide1 {
	@FXML private ImageView minorityReport;
	
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
		if(event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition() == null) {
			System.out.println("null");
			return;
		}
		
		CursorNodeFactory cursorNodeFactory = new CursorNodeFactory() {

			@Override
			public Node createCursor() {
				return new Rectangle(20.0, 50.0, Color.RED);
			}
		};
		
		//@formatter:off
		LeapApp.leapStageDecorator().cursorPaneConfigurationProperty().get()
			.fxCursors()
				.createOrUpdate("FINGER " + event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getId())
				.overwriteCursorNode().adjust(10, 10).cursorNodeFactory(cursorNodeFactory).save()
				.move(event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().getScreenPosition().getX(),
						event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().getScreenPosition().getY())
			.save()
		.save();
		
		LeapApp.leapStageDecorator().cursorPaneConfigurationProperty().get()
			.fxCursors()
				.createOrUpdate("FINGER " + event.getSkeleton().getHands().get(0).getFinger(FingerType.INDEX).getId())
				.overwriteCursorNode().adjust(10, 10).cursorNodeFactory(cursorNodeFactory).save()
				.move(event.getSkeleton().getHands().get(0).getFinger(FingerType.INDEX).getTipPosition().getScreenPosition().getX(),
						event.getSkeleton().getHands().get(0).getFinger(FingerType.INDEX).getTipPosition().getScreenPosition().getY())
			.save()
		.save();

		LeapApp.leapStageDecorator().cursorPaneConfigurationProperty().get()
			.fxCursors()
				.createOrUpdate("FINGER " + event.getSkeleton().getHands().get(0).getFinger(FingerType.MIDDLE).getId())
				.overwriteCursorNode().adjust(10, 10).cursorNodeFactory(cursorNodeFactory).save()
				.move(event.getSkeleton().getHands().get(0).getFinger(FingerType.MIDDLE).getTipPosition().getScreenPosition().getX(),
						event.getSkeleton().getHands().get(0).getFinger(FingerType.MIDDLE).getTipPosition().getScreenPosition().getY())
			.save()
		.save();
		//@formatter:on

		if(event.getGestures().contains("Slide1dragStart")) {
			startValue = (int) event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().getScreenPosition().getX();
			startValueY = (int) event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().getScreenPosition().getY();
		}
		
		if(event.getGestures().contains("Slide1drag")) {
			double xDistance = event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().yDistanceTo(
					event.getSkeleton().getHands().get(0).getFinger(FingerType.INDEX).getTipPosition());
			
			if(xDistance > 40 && !scaleOff) {
				minorityReport.setFitWidth(minorityReport.getScene().getWidth());
			} else if(xDistance > 15 && !scaleOff) {
				startValue = (int) event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().getScreenPosition().getX();
				startValueY = (int) event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().getScreenPosition().getY();
				minorityReport.setFitWidth(minorityReport.getScene().getWidth()/30*xDistance);
			} else {
				scaleOff = true;
				minorityReport.setFitWidth(minorityReport.getScene().getWidth()/30*15);
				minorityReport.setTranslateX(-(startValue - (int) event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().getScreenPosition().getX()));
				minorityReport.setTranslateY(-(startValueY - (int) event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition().getScreenPosition().getY()));
			}
			
			System.out.println(xDistance);
		}
	}
	
	public void activate() {
		SkeletonApp.addListener(this);
		SkeletonApp.addGestureProvider(new SimplePalmDragAndDropGestureProvider("Slide1"));
		minorityReport.fitWidthProperty().unbind();
	}
}
