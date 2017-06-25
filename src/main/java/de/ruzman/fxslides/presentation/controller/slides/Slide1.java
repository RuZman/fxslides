package de.ruzman.fxslides.presentation.controller.slides;

import javax.annotation.PostConstruct;

import de.ruzman.hui.OnUpdate;
import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

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

	@OnUpdate
	public void onUpdate(SkeletonEvent event) {
	}
	
	public void activate() {
		SkeletonApp.addListener(this);
		minorityReport.fitWidthProperty().unbind();
	}
}
