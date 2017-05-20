package de.ruzman.newfx.control;

import javafx.scene.layout.Pane;

public class CursorPane extends Pane {
	public CursorPane() {
		super();
		init();
	}

	private void init() {
		setPrefWidth(Double.MAX_VALUE);
		setPrefHeight(Double.MAX_VALUE);
		setBackground(null);
		setMouseTransparent(true);
		setPickOnBounds(true);
	}

	public void addCursor(FXCursor fxCursor) {
		if (fxCursor != null) {
			getChildren().add(fxCursor.getNode());
		}
	}
}