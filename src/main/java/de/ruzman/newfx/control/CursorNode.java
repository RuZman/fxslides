package de.ruzman.newfx.control;

import java.util.Objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CursorNode {
	private ObjectProperty<CursorNodeFactory> cursorNodeFactory = new SimpleObjectProperty<>();
	private DoubleProperty adjustX = new SimpleDoubleProperty();
	private DoubleProperty adjustY = new SimpleDoubleProperty();
	
	private CursorNode() {}
	
	public ReadOnlyObjectProperty<CursorNodeFactory> cursorNodeFactoryProperty() {
		return cursorNodeFactory;
	}
	
	public ReadOnlyDoubleProperty adjustXProperty() {
		return adjustX;
	}
	
	public ReadOnlyDoubleProperty adjustYProperty() {
		return adjustY;
	}
	
	public static class CusorNodeConfiguration<T> {
		private CursorNode cursorNode;
		private T parent;
		private CursorNodeFactory cursorNodeFactory;
		
		private double adjustX;
		private double adjustY;
		
		public CusorNodeConfiguration(T parent) {
			this.parent = Objects.requireNonNull(parent);
		}
		
		public CusorNodeConfiguration<T> adjust(double x, double y) {
			this.adjustX = x;
			this.adjustY = y;
			return this;
		}
		
		public CusorNodeConfiguration<T> cursorNodeFactory(CursorNodeFactory cursorNodeFactory) {
			this.cursorNodeFactory = Objects.requireNonNull(cursorNodeFactory);
			return this;
		}
		
		public CursorNode getInstance() {
			return cursorNode;
		}
		
		public T save() {
			if(cursorNode == null) {
				cursorNode = new CursorNode();
			}
			
			cursorNode.cursorNodeFactory.set(Objects.requireNonNull(cursorNodeFactory));
			cursorNode.adjustX.set(adjustX);
			cursorNode.adjustY.set(adjustY);
			return parent;
		}
	}

}
