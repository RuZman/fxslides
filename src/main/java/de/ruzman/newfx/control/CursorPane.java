package de.ruzman.newfx.control;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class CursorPane extends Pane {
	private FXCursor<?> fxCursor;
	private StackPane front;

	public CursorPane() {
		super();
		init();
		setMouseTransparent(true);
		setPickOnBounds(true);
	}

	private void init() {
		setPrefWidth(Double.MAX_VALUE);
		setPrefHeight(Double.MAX_VALUE);
		setBackground(null);
		front = new StackPane();
	}

	// FIXME: Workaround ... set cursor always on top ...
	public void addContent(Node node) {
		int index = 0;

		if (getChildren().contains(front)) {
			index++;
		}

		if (fxCursor != null) {
			index++;
		}

		getChildren().add(getChildren().size() - index, node);
	}

	public void addToFront(Node node) {
		if (!getChildren().contains(front)) {
			getChildren().add(front);
		}

		front.getChildren().add(node);
	}

	public void removeFromFront(Node node) {
		front.getChildren().remove(node);

		if (front.getChildren().isEmpty()) {
			getChildren().remove(front);
		}
	}

	public void setCursor(FXCursor<?> fxCursor) {
		if (fxCursor != null) {
			getChildren().remove(fxCursor.getNode());
			getChildren().add(fxCursor.getNode());
		}

		this.fxCursor = fxCursor;
	}
}