package de.ruzman.hui.skeleton;

import de.ruzman.common.Point;
import de.ruzman.hui.skeleton.Skeleton.Type;

public class Finger extends SkeletonPart {
	private FingerType fingerType;
	private Point tipPosition;
	
	private Finger(FingerBuilder fingerBuilder) {
		super(fingerBuilder);
		fingerType = fingerBuilder.fingerType;
		tipPosition = fingerBuilder.tipPosition;
	}

	public FingerType getFingerType() {
		return fingerType;
	}
	
	public Point getTipPosition() {
		return tipPosition;
	}

	public static class FingerBuilder extends SkeletonPartBuilder<FingerBuilder, Finger> {
		public FingerType fingerType;
		public Point tipPosition;
		
		public FingerBuilder(int id, FingerType fingerPart, FingerBuilder lastFingerBuilder) {
			this(id, fingerPart, lastFingerBuilder != null ? lastFingerBuilder.getInitializedObject() : null);
		}
		
		public FingerBuilder(int id, FingerType fingerPart, Finger anchestor) {
			super(id, Type.FINGER, anchestor);
			this.fingerType = fingerPart;
		}
		

		public FingerBuilder tipPosition(Point tipPosition) {
			this.tipPosition = tipPosition;
			return this;
		}
		

		public Finger create() {
			if(initializedObject == null) {
				return initializedObject = new Finger(this);
			}
			
			throw new RuntimeException();
		}
	}
}
