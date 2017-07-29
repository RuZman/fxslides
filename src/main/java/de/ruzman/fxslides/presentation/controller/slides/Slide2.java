package de.ruzman.fxslides.presentation.controller.slides;

import javax.annotation.PostConstruct;

import de.ruzman.fxslides.presentation.SimplePalmDragAndDropGestureProvider;
import de.ruzman.hui.OnUpdate;
import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.skeleton.FingerType;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

@FXMLController("../../fxml/slides/slide2.fxml")
public class Slide2 {
	@FXML private StackPane slide;
	private Box box;
	
	@PostConstruct
	public void init() {
		PhongMaterial material = new PhongMaterial(Color.BLACK);
		material.setSelfIlluminationMap(new Image("file:C:\\Users\\ruzmanz\\Pictures\\Avatar2.png"));
		
		box = new Box(1920, 1080, 1920);
		box.setRotationAxis(new Point3D(0, 1, 1));
		box.setRotate(90);
		box.setMaterial(material);
		
		
		slide.getChildren().add(box);
		
		slide.sceneProperty().addListener((a, b, c) -> {
			PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
			perspectiveCamera.setFieldOfView(60);
			c.setCamera(perspectiveCamera);
		});
		
		SkeletonApp.addListener(this);
		SkeletonApp.addGestureProvider(new SimplePalmDragAndDropGestureProvider("Slide2"));
	}
	
	boolean first = false;
	boolean secound = false;
	boolean stop = false;
	
	@OnUpdate
	public void onUpdate(SkeletonEvent event) {
		if(!box.isVisible() && !first && event.getSkeleton().getHands().size() == 1) {
			first = true;
			System.out.println("FIRST");
		}
		
		if(event.getSkeleton().getHands().size() != 2) {
			return;
		}
		
			double xDistance = event.getSkeleton().getHands().get(1).getPalmPosition().get().xDistanceTo(
					event.getSkeleton().getHands().get(0).getFinger(FingerType.THUMB).getTipPosition());
			
			if(xDistance < 100) {
				box.setWidth(1920*xDistance/100);
			}
			
			double yDistance = event.getSkeleton().getHands().get(1).getPalmPosition().get().yDistanceTo(
					event.getSkeleton().getHands().get(0).getPalmPosition().get());
			
			if(xDistance < 100) {
				box.setWidth(1920*xDistance/100);
				box.setDepth(1920*xDistance/100);
			}
			if(yDistance < 200) {
				box.setHeight(1080*yDistance/200);
			}
			
			if(!box.isVisible()) {
				double distance = event.getSkeleton().getHands().get(1).getPalmPosition().get().distanceTo(
						event.getSkeleton().getHands().get(0).getPalmPosition().get());
				if(first && !secound && distance < 480) {
					secound = true;
				}
				if(secound && !stop) {
					slide.setScaleX((1920-distance*4)/1920);
					slide.setScaleY((1080-distance*2.25)/1080);
					
					if((1920-distance*4)/1920 < 0.005) {
						slide.setScaleX(0.001);
						slide.setScaleY(0.001);
						stop = true;
					}
				}
			}
			
			if(xDistance < 50 && yDistance < 40) {
				box.setVisible(false);
			}
	}
}
