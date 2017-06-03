package de.ruzman.common;

import java.util.Optional;

import com.leapmotion.leap.Vector;

public class Point {
	public Optional<Point> source;
	
	private Vector absolutePosition;
	
	private Optional<TrackingBox> trackingBox;
	
	public Point(Point source, TrackingBox trackingBox, Vector absolutePosition) {
		this.source = Optional.ofNullable(source);
		this.absolutePosition = absolutePosition;
		this.trackingBox = Optional.ofNullable(trackingBox);
	}
	
	public Vector getAbsolutePosition() {
		if(!source.isPresent()) {
			return absolutePosition;
		}
		return absolutePosition.plus(source.get().getAbsolutePosition());
	}
	
	public Vector getScreenPosition() {
		if(!source.isPresent()) {
			throw new RuntimeException("Can't calculate screen position without source."); 
		}
		Vector screenPosition = new Vector();
		source.get().trackingBox.get().calcScreenPosition(absolutePosition, screenPosition);
		return screenPosition;
		
	}
}
