package de.ruzman.fxslides.presentation.controller.slides;

import java.awt.event.MouseMotionListener;

import de.ruzman.leap.LeapApp;
import de.ruzman.leap.event.PointEvent;
import de.ruzman.leap.event.PointMotionListener;
import de.ruzman.newfx.control.CursorNodeFactory;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

@FXMLController("../../fxml/slides/slide1.fxml")
public class Slide1 implements PointMotionListener {
    @FXML
    public void initialize() {
        LeapApp.getMotionRegistry().addListener(this);
    }

	@Override
	public void enteredViewoport(PointEvent event) {

	}

	@Override
	public void moved(PointEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leftViewport(PointEvent event) {
		LeapApp.setCursorNodeFactory(new CursorNodeFactory() {

			@Override
			public Circle createCursor() {
				return new Circle(0, 0, 18, Color.rgb(50, 50, 50));
			}
		});
	}
}
