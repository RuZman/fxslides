package de.ruzman.hui.skeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.ruzman.common.Point;
import de.ruzman.hui.skeleton.Finger.FingerBuilder;
import de.ruzman.hui.skeleton.Skeleton.Type;

public class Hand extends SkeletonPart {
	private Optional<Point> palmPosition;
	private Finger[] fingerIndex = new Finger[5];
	private List<Finger> fingers = new ArrayList<>();
	
	private Hand(HandBuilder handBuilder) {
		super(handBuilder);
		this.palmPosition = Optional.ofNullable(handBuilder.palmPosition);
		
		for(FingerBuilder fingerBuilder: handBuilder.fingerBuilders) {
			Finger finger = fingerBuilder.create();
			fingerIndex[fingerBuilder.fingerType.ordinal()] = finger;
			fingers.add(finger);
		}
	}
	
	public Optional<Point> getPalmPosition() {
		return palmPosition;
	}
	
	public Finger getFinger(FingerType fingerType) {
		return fingerIndex[fingerType.ordinal()];
	}
	
	public List<Finger> getFingers() {
		return fingers;
	}

	public static class HandBuilder extends SkeletonPartBuilder<HandBuilder, Hand> {
		private Point palmPosition = null;
		private List<FingerBuilder> fingerBuilders = new ArrayList<>();

		public HandBuilder(int id, HandBuilder lastHandBuilder) {
			this(id, lastHandBuilder != null ? lastHandBuilder.getInitializedObject() : null);
		}
		
		public HandBuilder(int id, Hand hand) {
			super(id, Type.HAND, hand);
		}
		
		public HandBuilder palmPosition(Point palmPosition) {
			this.palmPosition = palmPosition;
			return this;
		}
		
		public void addFinger(FingerBuilder fingerBuilder) {
			fingerBuilders.add(fingerBuilder);
		}
		
		@Override
		public Hand create() {
			if(initializedObject == null) {
				return initializedObject = new Hand(this);
			}
			throw new RuntimeException();
		}
	}
}
