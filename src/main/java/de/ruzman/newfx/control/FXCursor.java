package de.ruzman.newfx.control;

import java.util.Objects;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;

import de.ruzman.newfx.control.CursorNode.CusorNodeConfiguration;
import de.ruzman.newfx.control.CursorPane.CursorPaneConfiguration;
import de.ruzman.newfx.control.CursorPane.FXCursors;
import de.ruzman.newfx.event.CursorEvent;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.text.Text;

@SuppressWarnings("restriction")
public class FXCursor {
	private ObjectProperty<CusorNodeConfiguration<?>> cursorNodeConfiguration = new SimpleObjectProperty<>();
	private ObjectProperty<Node> node = new SimpleObjectProperty<>();
	
	private DoubleProperty x = new SimpleDoubleProperty(0);
	private DoubleProperty y = new SimpleDoubleProperty(0);

	private Node nodeToSendEvent;

	private FXCursor() {
		super();
	}

	private final void move() {
		setNodePosition();
		fireEvent();
	}

	private void setNodePosition() {
		node.get().setTranslateX(x.get() + cursorNodeConfiguration.get().getInstance().adjustXProperty().get());
		node.get().setTranslateY(y.get() + cursorNodeConfiguration.get().getInstance().adjustYProperty().get());
	}

	private void fireEvent() {
		double x = this.x.get();
		double y = this.y.get();
		
		Node nodeToSendEvent = getNodeToSendEvent(x, y);

		if (this.nodeToSendEvent == null && nodeToSendEvent == null) {
			// DO NOTHING
		} else if (Objects.equals(this.nodeToSendEvent, nodeToSendEvent)) {
			// nodeToSendEvent.fireEvent(new
			// CursorEvent(CursorEvent.CURSOR_MOVED, x, y));
		} else if (this.nodeToSendEvent != null && nodeToSendEvent != null) {
			node.get().fireEvent(new CursorEvent(CursorEvent.CURSOR_LEFT, this.nodeToSendEvent, x, y));
			node.get().fireEvent(new CursorEvent(CursorEvent.CURSOR_ENTERED, nodeToSendEvent, x, y));
		} else if (this.nodeToSendEvent == null) {
			node.get().fireEvent(new CursorEvent(CursorEvent.CURSOR_ENTERED, nodeToSendEvent, x, y));
		} else {
			// TODO: Is this Case possible?
		}

		this.nodeToSendEvent = nodeToSendEvent;
	}

	private Node getNodeToSendEvent(double x, double y) {
		Node nodeToSendEvent = null;

		final PickResultChooser result = new PickResultChooser();
		PickRay rayPick = new PickRay(x, y, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		node.get().getScene().getRoot().impl_pickNode(rayPick, result);
		nodeToSendEvent = result.getIntersectedNode();

		if (nodeToSendEvent != null && !nodeToSendEvent.equals(nodeToSendEvent.getScene().getRoot())
				&& (nodeToSendEvent.isMouseTransparent() || nodeToSendEvent instanceof Text)) {
			Node controlNotToSendEvent = nodeToSendEvent.getParent();
			while (controlNotToSendEvent != null
					&& (controlNotToSendEvent.isMouseTransparent() || controlNotToSendEvent instanceof Text)) {
				controlNotToSendEvent = controlNotToSendEvent.getParent();
			}
			nodeToSendEvent = controlNotToSendEvent;
		}

		return nodeToSendEvent;
	}
	
	public ReadOnlyObjectProperty<Node> nodeProperty() {
		return node;
	}
	
	public static class FXCursorConfiguration {
		private FXCursors parent;
		private FXCursor fxCursor;
		private boolean isMoving;
		private boolean shouldCreateNewNode;
		private double x;
		private double y;
		private CusorNodeConfiguration<CursorPaneConfiguration> defaultCursorNode;
		private CusorNodeConfiguration<FXCursorConfiguration> cursorNode;
		
		public FXCursorConfiguration(FXCursors parent, CusorNodeConfiguration<CursorPaneConfiguration> defaultCursorNode) {
			this.defaultCursorNode = Objects.requireNonNull(defaultCursorNode);
			this.parent = Objects.requireNonNull(parent);
			shouldCreateNewNode = true;
		}
		
		public FXCursorConfiguration move(double x, double y) {
			this.x = x;
			this.y = y;
			this.isMoving = true;
			return this;
		}
		
		public CusorNodeConfiguration<FXCursorConfiguration> overwriteCursorNode() {
			if(cursorNode == null) {
				cursorNode = new CusorNodeConfiguration<FXCursorConfiguration>(this);
			}

			shouldCreateNewNode = true; // TODO: Erstellt unnötig eine neue Node, wenn diese Property nicht gespeichert wird.
			return cursorNode;
		}
		
		public FXCursors save() {
			if(fxCursor == null) {
				isMoving = false;
				fxCursor = new FXCursor();
			}
			
			fxCursor.x.set(x);
			fxCursor.y.set(y);
			
			if(cursorNode == null) {
				fxCursor.cursorNodeConfiguration.set(defaultCursorNode);
			} else {
				fxCursor.cursorNodeConfiguration.set(cursorNode);
			}
			
			if(shouldCreateNewNode) {
				if(fxCursor.node.get() != null) {
					((CursorPane) fxCursor.node.get().getParent()).getChildren().remove(fxCursor.node.get());
				}
				fxCursor.node.set(fxCursor.cursorNodeConfiguration.get().getInstance().cursorNodeFactoryProperty().get().createCursor());
				shouldCreateNewNode = false;
			}
			
			if(isMoving) {
				fxCursor.move();
				isMoving = false;
			}
			
			return parent;
		}
		
		public FXCursor instance() {
			return fxCursor;
		}
	}
}
