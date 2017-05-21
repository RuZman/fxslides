package de.ruzman.hui.skeleton;

import de.ruzman.hui.skeleton.Skeleton.Type;

public class Finger extends SkeletonPart {

	private Finger(FingerBuilder fingerBuilder) {
		super(fingerBuilder);
	}

	public static class FingerBuilder extends SkeletonPartBuilder<FingerBuilder, Finger> {
		public FingerBuilder(int id, FingerBuilder lastFingerBuilder) {
			this(id, lastFingerBuilder != null ? lastFingerBuilder.getInitializedObject() : null);
		}
		
		public FingerBuilder(int id, Finger anchestor) {
			super(id, Type.FINGER, anchestor);
		}

		public Finger create() {
			if(initializedObject == null) {
				return initializedObject = new Finger(this);
			}
			throw new RuntimeException();
		}
	}
}
