package de.ruzman.fxslides.presentation.controller.slides;

import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;

@FXMLController("../../fxml/slides/slide1.fxml")
public class Slide1 implements SkeletonListener {
    @FXML
    public void initialize() {
        SkeletonApp.addListener(this);
    }

	@Override
	public void onUpdate(SkeletonEvent event) {
		if(event.getSkeleton().hasLeft()) {
			System.out.println(event.getSkeleton().getId());
		}
	}
}
