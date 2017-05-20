package de.ruzman.hui.skeleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.ruzman.hui.skeleton.Hand.HandBuilder;

public class Skeleton {
	private Set<Integer> handIds = new HashSet<>();
	private boolean hasLeft;

	public enum Type {
		SKELETON, HAND
	}

	private List<Hand> hands;

	private Skeleton(SkeletonBuilder skeletonBuilder) {
		hands = skeletonBuilder.handBuilders.stream().map(handBuilder -> handBuilder.createHand())
				.collect(Collectors.toList());
		this.hasLeft = skeletonBuilder.hasLeft;
	}

	public boolean hasLeft() {
		return hasLeft;
	}
	
	public List<Hand> getHands() {
		return hands;
	}

	public static class SkeletonBuilder {
		protected List<HandBuilder> handBuilders = new ArrayList<>();
		private boolean hasLeft = false;

		public SkeletonBuilder addHand(Hand.HandBuilder handBuilder) {
			handBuilders.add(handBuilder);
			return this;
		}

		public SkeletonBuilder hasLeft(boolean hasLeft) {
			this.hasLeft = hasLeft;
			return this;
		}
		
		public Skeleton createSkeleton() {
			return new Skeleton(this);
		}
	}

	public boolean containsId(Type type, int id) {
		switch (type) {
		case HAND:
			return handIds.contains(id);
		default:
			throw new RuntimeException("Invalid type");
		}
	}

	public Integer getId() {
		return 0;
	}
}
