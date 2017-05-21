package de.ruzman.hui.skeleton;

import de.ruzman.hui.skeleton.Skeleton.Type;

public class Finger extends SkeletonPart {

	private Finger(FingerBuilder fingerBuilder) {
		super(fingerBuilder);
	}

	public static class FingerBuilder extends SkeletonPartBuilder<FingerBuilder, Finger> {
		public FingerBuilder(int id) {
			super(id, Type.FINGER);
		}

		public Finger create() {
			return new Finger(this);
		}
	}
}
