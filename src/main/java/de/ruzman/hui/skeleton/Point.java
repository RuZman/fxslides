package de.ruzman.hui.skeleton;

import java.util.Objects;
import java.util.Optional;

import com.leapmotion.leap.Vector;

import de.ruzman.leap.TrackingBox;

public class Point {
	public Optional<Point> source;
	
	private Vector absolutePosition;
	
	private boolean hasEntered;
	private boolean hasLeft;
	
	private Optional<TrackingBox> trackingBox;
	
	public Point(Point source, TrackingBox trackingBox, Vector absolutePosition, boolean hasEntered, boolean hasLeft) {
		this.source = Optional.ofNullable(source);
		this.absolutePosition = absolutePosition;
		this.hasEntered = hasEntered;
		this.hasLeft = hasLeft;
		this.trackingBox = Optional.ofNullable(trackingBox);
	}
	
	public Vector getAbsolutepPosition() {
		if(!source.isPresent()) {
			return absolutePosition;
		}
		return absolutePosition.plus(source.get().getAbsolutepPosition());
	}
	
	public Vector getScreenPosition() {
		if(!source.isPresent()) {
			throw new RuntimeException("Can't calculate screen position without source."); 
		}
		Vector screenPosition = new Vector();
		source.get().trackingBox.get().calcScreenPosition(absolutePosition, screenPosition);
		return screenPosition;
		
	}
	
	public boolean hasEntered() {
		return hasEntered;
	}
	
	public boolean isMoving() {
		return !hasEntered && !hasLeft; 
	}
	
	public boolean hasLeft() {
		return hasLeft;
	}
}
