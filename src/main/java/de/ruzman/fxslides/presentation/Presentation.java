package de.ruzman.fxslides.presentation;

import java.util.ResourceBundle;

import de.ruzman.fxslides.presentation.controller.slides.Slide1;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Presentation extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Flow flow = new Flow(Slide1.class);
			
			primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			primaryStage.setFullScreen(true);
			
			flow.startInStage(primaryStage);
			
			String css = Presentation.class.getResource("styles.css").toExternalForm();
			primaryStage.getScene().getStylesheets().add(css);			
		} catch(FlowException ex) {
			ex.printStackTrace();
		}
	}
}
