package de.ruzman.newfx.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.ruzman.newfx.control.CursorNode.CusorNodeConfiguration;
import de.ruzman.newfx.control.FXCursor.FXCursorConfiguration;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.scene.layout.Pane;

public class CursorPane extends Pane {
	private MapProperty<String, FXCursorConfiguration> fxCursors = new SimpleMapProperty<>(FXCollections.observableHashMap());
	
	private CursorPane() {
		super();
		
		setPrefWidth(Double.MAX_VALUE);
		setPrefHeight(Double.MAX_VALUE);
		setBackground(null);
		setMouseTransparent(true);
		setPickOnBounds(true);
	}

	private void addCursor(FXCursor fxCursor) {
		if (fxCursor != null) {
			getChildren().add(fxCursor.nodeProperty().get()); // FIXME: Use Property instead of fixed valueD
			fxCursor.nodeProperty().addListener(e -> {
				getChildren().add(fxCursor.nodeProperty().get());
			});
		}
	}
	
	private void removeCursor(FXCursor fxCursor) {
		if (fxCursor != null) {
			getChildren().remove(fxCursor.nodeProperty().get()); // FIXME: Use Property instead of fixed value
		}
	}
	
	public static class CursorPaneConfiguration {
		private CursorPane cursorPane;		
		private FXCursors fxCursors = new FXCursors(this);
		private CusorNodeConfiguration<CursorPaneConfiguration> defaultCursorNode;
		
		public FXCursors fxCursors() {
			return fxCursors;
		}
		
		public FXCursorConfiguration fxCursor(String id) {
			return fxCursors.get(Objects.requireNonNull(id));
		}
		
		public CusorNodeConfiguration<CursorPaneConfiguration> defaultCursorNode() {
			if(defaultCursorNode == null) {
				defaultCursorNode = new CusorNodeConfiguration<>(this);
			}
			return defaultCursorNode;
		}
		
		public CursorPane instance() {
			return cursorPane;
		}
		
		public CursorPaneConfiguration save() {
			if(cursorPane == null) {
				cursorPane = new CursorPane();
			}
			
			return this;
		}
	}
	
	public static class FXCursors {
		private CursorPaneConfiguration parent;
		private Map<String, FXCursorConfiguration> fxCursorConfigurations = new HashMap<>();
		private List<String> createFXCursorConfigurations = new LinkedList<>();
		private List<String> removeFXCursorConfigurations = new LinkedList<>();

		private FXCursors(CursorPaneConfiguration parent) {
			this.parent = parent;
		}
		
		public FXCursorConfiguration createOrUpdate(String id) {
			if(!fxCursorConfigurations.containsKey(id)) {
				FXCursorConfiguration fxCursorConfiguration = new FXCursorConfiguration(this, parent.defaultCursorNode);
				fxCursorConfigurations.put(id, fxCursorConfiguration);
				createFXCursorConfigurations.add(id);
			}
			
			return fxCursorConfigurations.get(id);
		}
		
		public FXCursorConfiguration get(String id) {
			return fxCursorConfigurations.get(id);
		}
		
		public FXCursors remove(String id) {
			if(fxCursorConfigurations.containsKey(id)) {
				removeFXCursorConfigurations.add(id);
			}
			return this;
		}
		
		public CursorPaneConfiguration save() {
			for(String key: createFXCursorConfigurations) {
				FXCursorConfiguration fxCursorConfiguration = fxCursorConfigurations.get(key);
				parent.cursorPane.fxCursors.put(key, fxCursorConfiguration);
				parent.instance().addCursor(fxCursorConfiguration.instance());
			}
			createFXCursorConfigurations.clear();
			
			for(String key: removeFXCursorConfigurations) {
				fxCursorConfigurations.remove(key);
				parent.cursorPane.removeCursor(parent.cursorPane.fxCursors.get(key).instance());
				parent.cursorPane.fxCursors.remove(key);
			}
			removeFXCursorConfigurations.clear();
			
			return parent;
		}
	}
}