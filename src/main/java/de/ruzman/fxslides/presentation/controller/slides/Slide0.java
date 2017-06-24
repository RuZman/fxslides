package de.ruzman.fxslides.presentation.controller.slides;

import javax.annotation.PostConstruct;

import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

@FXMLController("../../fxml/slides/slide0.fxml")
public class Slide0 {
	@FXML private StackPane slide;
	@FXML private Circle msgCircle;
	@FXML private VBox title;
	
	@PostConstruct
	public void init() {
		slide.sceneProperty().addListener((observable, oldValue, newValue) -> this.scaleToScreen(newValue));
		StackPane.setAlignment(msgCircle, Pos.CENTER_RIGHT);
		StackPane.setAlignment(title, Pos.CENTER_LEFT);
	}
	
	private void scaleToScreen(Scene scene) {
		if(scene == null) {
			return;
		}
		
		msgCircle.radiusProperty().bind(scene.heightProperty().divide(100).multiply(13));
		msgCircle.translateXProperty().bind(scene.heightProperty().divide(100).multiply(-6.5));
		title.translateXProperty().bind(scene.heightProperty().divide(100).multiply(13));
		title.translateYProperty().bind(scene.heightProperty().divide(100).multiply(66));
	}
}
