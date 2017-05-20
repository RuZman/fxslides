package de.ruzman.leap;

import static de.ruzman.leap.LeapApp.getTrackedAreaHeight;
import static de.ruzman.leap.LeapApp.getTrackedAreaWidth;

import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Vector;

public class TrackingBox {
	private static final Vector DEFAULT_BOX;
	private static final Vector DEFAULT_ORIGIN;
	
	static {
		InteractionBox iBox = LeapApp.getController().frame().interactionBox();

		DEFAULT_ORIGIN = new Vector(-(iBox.width()/2)-iBox.center().getX(), 
				-(iBox.height()/2)+iBox.center().getY(), -(iBox.depth()/2)-iBox.center().getZ());
		DEFAULT_BOX = new Vector(iBox.width(), iBox.height(), iBox.depth());
	}
	private Vector box;
	private Vector origin;
	private Vector centroid;

	private Vector centroidOrigin;
	private Vector halfBox;
	
	public TrackingBox() {
		this(DEFAULT_BOX, DEFAULT_ORIGIN);
	}

	public TrackingBox(float width, float height, float depth) {
		this(new Vector(width, height, depth));
	}
	
	public TrackingBox(Vector box) {
		this(box, new Vector(-box.getX()/2, 100, box.getZ()/2));
	}

	public TrackingBox(Vector box, Vector origin) {
		setBox(box);
		setOrigin(origin);
	}

	public void setBox(Vector box) {
		this.box = new Vector(box);
		halfBox = box.times(0.5f);
	}

	public void setOrigin(Vector origin) {
		this.origin = new Vector(origin);
		setCentroid();
	}

	private void setCentroid() {
		centroid = new Vector(box.getX()/2, box.getY()/2, box.getZ()/2);
		centroidOrigin = origin.plus(centroid);
	}
	
	public void calcNormalizedPoint(Vector point, Vector normalizedPoint) {
		normalizedPoint.setX((point.getX()-centroidOrigin.getX())/box.getX());
		normalizedPoint.setY((point.getY()-centroidOrigin.getY())/box.getX());
		normalizedPoint.setZ((point.getZ()-centroidOrigin.getZ())/box.getX());
	}
	
	public void calcScreenPosition(Vector point, Vector screenPosition) {
		screenPosition.setX((float) ((point.getX()-origin.getX())*getTrackedAreaWidth() / box.getX()));
		screenPosition.setY((float) (getTrackedAreaHeight() - ((point.getY()-origin.getY())*getTrackedAreaHeight() / box.getY())));
		screenPosition.setZ((point.getZ()-origin.getX()));
	}
	
	public void calcZone(Vector point, Vector zone) {
		zone.setX((point.getX()-centroidOrigin.getX())/halfBox.getX());
		zone.setY((point.getY()-centroidOrigin.getY())/halfBox.getY());
		zone.setZ((point.getZ()-centroidOrigin.getZ())/halfBox.getZ());
	}
}