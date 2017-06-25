package de.ruzman.hui.skeleton;

import de.ruzman.hui.skeleton.Skeleton.Type;

public class Finger extends SkeletonPart {
	private FingerType fingerType;
	
	private Finger(FingerBuilder fingerBuilder) {
		super(fingerBuilder);
		fingerType = fingerBuilder.fingerType;
	}

	public FingerType getFingerType() {
		return fingerType;
	}

	public static class FingerBuilder extends SkeletonPartBuilder<FingerBuilder, Finger> {
		public FingerType fingerType;
		
		public FingerBuilder(int id, FingerType fingerPart, FingerBuilder lastFingerBuilder) {
			this(id, fingerPart, lastFingerBuilder != null ? lastFingerBuilder.getInitializedObject() : null);
		}
		
		public FingerBuilder(int id, FingerType fingerPart, Finger anchestor) {
			super(id, Type.FINGER, anchestor);
			this.fingerType = fingerPart;
		}

		public Finger create() {
			if(initializedObject == null) {
				return initializedObject = new Finger(this);
			}
			
			throw new RuntimeException();
		}
	}
}
