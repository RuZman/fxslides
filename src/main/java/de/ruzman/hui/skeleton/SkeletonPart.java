package de.ruzman.hui.skeleton;

import de.ruzman.hui.skeleton.Skeleton.Type;

public abstract class SkeletonPart {
	private int id;
	private boolean hasEntered;
	private boolean hasLeft;
	
	public SkeletonPart(SkeletonPartBuilder<?, ? extends SkeletonPart> skeletonPartBuilder) {
		this.id = skeletonPartBuilder.id;
		this.hasEntered = skeletonPartBuilder.hasEntered;
		this.hasLeft = skeletonPartBuilder.hasLeft;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean hasEntered() {
		return hasEntered;
	}

	public boolean hasLeft() {
		return hasLeft;
	}
	
	public static abstract class SkeletonPartBuilder<E, T extends SkeletonPart> {
		private int id;
		private boolean hasEntered = false;
		private boolean hasLeft = false;

		public SkeletonPartBuilder(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		
		@SuppressWarnings("unchecked")
		public E hasEntered(World lastWorld) {
			hasEntered = lastWorld == null || !lastWorld.containsSkeletonPart(Type.HAND, id).isPresent();
			return (E) this;
		}
		
		@SuppressWarnings("unchecked")
		public E hasLeft(boolean hasLeft) {
			this.hasLeft = hasLeft;
			return (E) this;
		}
		
		public abstract T create();
	}
}
