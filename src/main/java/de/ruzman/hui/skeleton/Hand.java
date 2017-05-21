package de.ruzman.hui.skeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.ruzman.hui.skeleton.Finger.FingerBuilder;
import de.ruzman.hui.skeleton.Skeleton.Type;

public class Hand extends SkeletonPart {
	private Optional<Point> palmPosition;
	private List<Finger> fingers;
	
	private Hand(HandBuilder handBuilder) {
		super(handBuilder);
		this.palmPosition = Optional.ofNullable(handBuilder.palmPosition);
		fingers = handBuilder.fingerBuilders.stream().map(fingerBuilder -> fingerBuilder.create())
				.collect(Collectors.toList());
	}
	
	public Optional<Point> getPalmPosition() {
		return palmPosition;
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
