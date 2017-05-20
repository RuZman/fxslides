package de.ruzman.hui.skeleton;

import java.util.Optional;

import de.ruzman.hui.skeleton.Skeleton.Type;

public class Hand {
	private int id;
	private Optional<Point> palmPosition;
	private boolean hasEntered;
	private boolean hasLeft;
	
	private Hand(HandBuilder handBuilder) {
		this.id = handBuilder.id;
		this.palmPosition = Optional.ofNullable(handBuilder.palmPosition);
		this.hasEntered = handBuilder.hasEntered;
		this.hasLeft = handBuilder.hasLeft;
	}
	
	public Optional<Point> getPalmPosition() {
		return palmPosition;
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

	public static class HandBuilder {
		private int id;
		private Point palmPosition = null;
		private boolean hasEntered = false;
		private boolean hasLeft = false;
		
		public HandBuilder(int id) {
			this.id = id;
		}
		
		public HandBuilder palmPosition(Point palmPosition) {
			this.palmPosition = palmPosition;
			return this;
		}
		
		public HandBuilder hasEntered(World lastWorld) {
			hasEntered = lastWorld == null || !lastWorld.containsSkeletonPart(Type.HAND, id).isPresent();
			return this;
		}
		
		public Hand createHand() {
			return new Hand(this);
		}

		public HandBuilder hasLeft(boolean hasLeft) {
			this.hasLeft = hasLeft;
			return this;
		}
	}
}
