package de.ruzman.hui.skeleton;

import java.util.Optional;

import de.ruzman.hui.skeleton.Skeleton.Type;

public abstract class SkeletonPart {
	private int id;
	private Type type;
	private boolean hasEntered;
	private boolean hasLeft;
	private Optional<? extends SkeletonPart> ancestor;
	
	public SkeletonPart(SkeletonPartBuilder<?, ? extends SkeletonPart> skeletonPartBuilder) {
		this.id = skeletonPartBuilder.id;
		this.type = skeletonPartBuilder.type;
		this.hasEntered = skeletonPartBuilder.hasEntered;
		this.hasLeft = skeletonPartBuilder.hasLeft;
		this.ancestor = skeletonPartBuilder.ancestor;
	}
	
	public int getId() {
		return id;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getIdentificator() {
		return type.name() + id;
	}
	
	public boolean hasEntered() {
		return hasEntered;
	}

	public boolean hasLeft() {
		return hasLeft;
	}
	
	public Optional<? extends SkeletonPart> getAncestor() {
		return ancestor;
	}
	
	public static abstract class SkeletonPartBuilder<E, T extends SkeletonPart> {
		T initializedObject;
		
		private int id;
		private Type type;
		private boolean hasEntered = false;
		private boolean hasLeft = false;
		private Optional<T> ancestor;

		public SkeletonPartBuilder(int id, Type type, T ancestor) {
			this.id = id;
			this.type = type;
			this.ancestor = Optional.ofNullable(ancestor);
			this.hasEntered = ancestor == null;
		}
		
		public int getId() {
			return id;
		}
		
		public Type getType() {
			return type;
		}
		
		public String getIdentificator() {
			return type.name() + id;
		}
		
		public T getInitializedObject() {
			return initializedObject;
		}
		
		@SuppressWarnings("unchecked")
		public E hasLeft(boolean hasLeft) {
			this.hasLeft = hasLeft;
			return (E) this;
		}
		
		public abstract T create();
	}
}
