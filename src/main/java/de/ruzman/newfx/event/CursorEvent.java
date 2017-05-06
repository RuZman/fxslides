package de.ruzman.newfx.event;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

@SuppressWarnings("serial")
public class CursorEvent extends Event {
	public static final EventType<CursorEvent> ANY = new EventType<>(Event.ANY, "ANY");
	public static final EventType<CursorEvent> CURSOR_ENTERED = new EventType<>(ANY, "CURSOR_ENTERED");
	public static final EventType<CursorEvent> CURSOR_MOVED = new EventType<>(ANY, "CURSOR_MOVED");
	public static final EventType<CursorEvent> CURSOR_LEFT = new EventType<>(ANY, "CURSOR_LEFT");
	public static final EventType<CursorEvent> CURSOR_CLICKED = new EventType<>(ANY, "CURSOR_CLICKED");

	private Node targetNode;

	private double x;
	private double y;

	public CursorEvent(EventType<? extends Event> eventType, Node targetNode, double x, double y) {
		super(eventType);

		this.targetNode = targetNode;
		this.x = x;
		this.y = y;
	}

	public Node getTragetNode() {
		return targetNode;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}