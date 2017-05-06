package de.ruzman.fxslides.presentation;

import static java.util.Locale.GERMAN;
import static javafx.scene.input.KeyCombination.NO_MATCH;

import java.util.ResourceBundle;

import de.ruzman.fxslides.presentation.controller.slides.Slide1;
import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.flow.FlowException;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Presentation extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			String baseName = getClass().getPackage().getName() + ".labels.labels";
			
			ViewConfiguration viewConfiguration = new ViewConfiguration();
			viewConfiguration.setResources(ResourceBundle.getBundle(baseName, GERMAN));
			
			FlowFixed flow = new FlowFixed(Slide1.class, viewConfiguration);
			
			primaryStage.setFullScreenExitKeyCombination(NO_MATCH);
			primaryStage.setFullScreen(true);
			
			flow.startInStage(primaryStage);
			
			String css = Presentation.class.getResource("style.css").toExternalForm();
			primaryStage.getScene().getStylesheets().add(css);			
		} catch(FlowException ex) {
			ex.printStackTrace();
		}
	}
}
