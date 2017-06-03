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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CursorPane extends Pane {
	private MapProperty<String, FXCursorConfiguration> fxCursors = new SimpleMapProperty<>(FXCollections.observableHashMap());
	
	private CursorPane(Stage stage) {
		super();
		
		setPrefWidth(Double.MAX_VALUE);
		setPrefHeight(Double.MAX_VALUE);
		setBackground(null);
		setMouseTransparent(true);
		setPickOnBounds(true);
		
		decorateScenceOnChange(stage);
	}
	
	private void decorateScenceOnChange(Stage stage) {
		stage.sceneProperty().addListener(new ChangeListener<Scene>() {
			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
				decorateStage(stage, newValue.getRoot());

				newValue.getRoot().getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
					@Override
					public void onChanged(Change<? extends Node> c) {
						c.next();
						if (c.wasAdded()) {
							decorateStage(stage, c.getAddedSubList().get(0));
						}
					}
				});
			}
		});
	}

	private void decorateStage(Stage stage, Node root) {
		stage.getScene().setRoot(new StackPane(root, this));
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
	
	public static class CursorPaneConfiguration<T> {
		private T parent;
		private Stage stage;
		private CursorPane cursorPane;		
		private FXCursors fxCursors = new FXCursors(this);
		private CusorNodeConfiguration<CursorPaneConfiguration<T>> defaultCursorNode;
		
		public CursorPaneConfiguration(T parent) {
			this.parent = parent;
		}
		
		public FXCursors fxCursors() {
			return fxCursors;
		}
		
		public CursorPaneConfiguration<T> stage(Stage stage) {
			this.stage = stage;
			return this;
		}
		
		public FXCursorConfiguration fxCursor(String id) {
			return fxCursors.get(Objects.requireNonNull(id));
		}
		
		public CusorNodeConfiguration<CursorPaneConfiguration<T>> defaultCursorNode() {
			if(defaultCursorNode == null) {
				System.out.println("Default Cursor setup");
				defaultCursorNode = new CusorNodeConfiguration<>(this);
			}
			return defaultCursorNode;
		}
		
		public CursorPane instance() {
			return cursorPane;
		}
		
		public T save() {
			if(cursorPane == null) {
				cursorPane = new CursorPane(Objects.requireNonNull(stage));
			}
			
			return parent;
		}
	}
	
	public static class FXCursors {
		private CursorPaneConfiguration<?> parent;
		private Map<String, FXCursorConfiguration> fxCursorConfigurations = new HashMap<>();
		private List<String> createFXCursorConfigurations = new LinkedList<>();
		private List<String> removeFXCursorConfigurations = new LinkedList<>();

		private FXCursors(CursorPaneConfiguration<?> parent) {
			this.parent = parent;
		}
		
		public FXCursorConfiguration createOrUpdate(String id) {
			System.out.println("pre inti?");
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
		
		public CursorPaneConfiguration<?> save() {
			for(String key: createFXCursorConfigurations) {
				System.out.println("Add cursor");
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