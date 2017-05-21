package de.ruzman.hui.skeleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.ruzman.hui.skeleton.Hand.HandBuilder;
import de.ruzman.hui.util.IdGenerator;

public class Skeleton extends SkeletonPart {
	private Set<Integer> handIds = new HashSet<>();

	public enum Type {
		SKELETON, HAND
	}

	private List<Hand> hands;

	private Skeleton(SkeletonBuilder skeletonBuilder) {
		// FIXME: Set Skeleton hasEntered
		super(skeletonBuilder);
		hands = skeletonBuilder.handBuilders.stream().map(handBuilder -> handBuilder.create())
				.collect(Collectors.toList());
	}
	
	public List<Hand> getHands() {
		return hands;
	}

	public boolean containsId(Type type, int id) {
		switch (type) {
		case HAND:
			return handIds.contains(id);
		case SKELETON:
			return getId() == id;
		default:
			throw new RuntimeException("Invalid type");
		}
	}

	public static class SkeletonBuilder extends SkeletonPartBuilder<SkeletonBuilder, Skeleton>{
		private List<HandBuilder> handBuilders = new ArrayList<>();

		public SkeletonBuilder() {
			this(Optional.empty());
		}
		
		public SkeletonBuilder(Optional<Integer> id) {
			super(id.isPresent() ? id.get() : IdGenerator.generateId());
		}
		
		public SkeletonBuilder addHand(Hand.HandBuilder handBuilder) {
			handBuilders.add(handBuilder);
			return this;
		}

		public List<HandBuilder> getHandBuilders() {
			return handBuilders;
		}
		
		@Override
		public Skeleton create() {
			return new Skeleton(this);
		}
	}

}
