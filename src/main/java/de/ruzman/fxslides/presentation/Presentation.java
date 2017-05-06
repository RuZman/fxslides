package de.ruzman.fxslides.presentation;

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
		primaryStage.setScene(new Scene(new Group()));
		
		primaryStage.setHeight(768);
		primaryStage.setWidth(1376);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		primaryStage.setFullScreen(true);
		
		String css = Presentation.class.getResource("styles.css").toExternalForm();
		primaryStage.getScene().getStylesheets().add(css);
		
		primaryStage.show();
	}
}
