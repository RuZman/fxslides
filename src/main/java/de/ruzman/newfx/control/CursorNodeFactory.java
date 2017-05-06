package de.ruzman.newfx.control;

import javafx.scene.Node;

public interface CursorNodeFactory<T extends Node> {
	public T createCursor();
}
