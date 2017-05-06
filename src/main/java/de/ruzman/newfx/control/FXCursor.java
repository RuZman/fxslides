package de.ruzman.newfx.control;

import java.util.Objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.text.Text;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;

import de.ruzman.newfx.event.CursorEvent;

@SuppressWarnings("restriction")
public class FXCursor<T extends Node> {
	CursorNodeFactory<T> cursorNodeFactory;
	private T node;
	private boolean isVisible = true;

	protected DoubleProperty x = new SimpleDoubleProperty(0);
	protected DoubleProperty y = new SimpleDoubleProperty(0);

	protected DoubleProperty adjustX = new SimpleDoubleProperty(0);
	protected DoubleProperty adjustY = new SimpleDoubleProperty(0);

	protected Node nodeToSendEvent;

	public void setAdjustX(double value) {
		adjustX.set(value);
	}

	public void setAdjustY(double value) {
		adjustY.set(value);
	}

	public final void move(double x, double y) {
		if (isVisible) {
			this.x.set(x);
			this.y.set(y);

			calcPosition();

			fireEvent(x, y);
		}
	}

	protected void calcPosition() {
		if (node != null) {
			node.setTranslateX(x.get() + adjustX.get());
			node.setTranslateY(y.get() + adjustY.get());
		}
	}

	private void fireEvent(double x, double y) {
		Node nodeToSendEvent = getNodeToSendEvent(x, y);

		if (this.nodeToSendEvent == null && nodeToSendEvent == null) {
			// DO NOTHING
		} else if (Objects.equals(this.nodeToSendEvent, nodeToSendEvent)) {
			// nodeToSendEvent.fireEvent(new
			// CursorEvent(CursorEvent.CURSOR_MOVED, x, y));
		} else if (this.nodeToSendEvent != null && nodeToSendEvent != null) {
			node.fireEvent(new CursorEvent(CursorEvent.CURSOR_LEFT, this.nodeToSendEvent, x, y));
			node.fireEvent(new CursorEvent(CursorEvent.CURSOR_ENTERED, nodeToSendEvent, x, y));
		} else if (this.nodeToSendEvent == null) {
			// nodeToSendEvent.fireEvent(new
			// CursorEvent(CursorEvent.CURSOR_ENTERED, x, y));
			node.fireEvent(new CursorEvent(CursorEvent.CURSOR_ENTERED, nodeToSendEvent, x, y));
		} else {
			// FIXME: What is this case?
		}

		this.nodeToSendEvent = nodeToSendEvent;
	}

	protected Node getNodeToSendEvent(double x, double y) {
		Node nodeToSendEvent = null;

		if (isVisible && node != null) {
			final PickResultChooser result = new PickResultChooser();
			PickRay rayPick = new PickRay(x, y, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			node.getScene().getRoot().impl_pickNode(rayPick, result);
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
		}

		return nodeToSendEvent;
	}

	public T getNode() {
		return (T) node;
	}

	public void setCursorNodeFactory(CursorNodeFactory<T> cursorNodeFactory) {
		this.cursorNodeFactory = cursorNodeFactory;
		initNewNode();
	}

	private void initNewNode() {
		T newNode = (T) cursorNodeFactory.createCursor();

		if (newNode == null) {
			throw new IllegalArgumentException();
		}

		node = newNode;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;

		if (node != null) {
			node.setVisible(isVisible);
			if (!isVisible) {
				nodeToSendEvent = null;
			}
		}
	}
}
