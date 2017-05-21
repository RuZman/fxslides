package de.ruzman.hui.skeleton;

public class Finger extends SkeletonPart {

	private Finger(FingerBuilder fingerBuilder) {
		super(fingerBuilder);
	}

	public static class FingerBuilder extends SkeletonPartBuilder<FingerBuilder, Finger> {
		public FingerBuilder(int id) {
			super(id);
		}

		public Finger create() {
			return new Finger(this);
		}
	}
}
