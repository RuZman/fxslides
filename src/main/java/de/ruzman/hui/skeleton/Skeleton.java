package de.ruzman.hui.skeleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.ruzman.hui.skeleton.Hand.HandBuilder;
import de.ruzman.hui.skeleton.Skeleton.SkeletonBuilder;
import de.ruzman.hui.util.IdGenerator;

public class Skeleton {
	private int id;
	private Set<Integer> handIds = new HashSet<>();
	private boolean hasLeft;

	public enum Type {
		SKELETON, HAND
	}

	private List<Hand> hands;

	private Skeleton(SkeletonBuilder skeletonBuilder) {		
		hands = skeletonBuilder.handBuilders.stream().map(handBuilder -> handBuilder.createHand())
				.collect(Collectors.toList());
		hasLeft = skeletonBuilder.hasLeft;
		id = skeletonBuilder.id;
	}

	public boolean hasLeft() {
		return hasLeft;
	}
	
	public List<Hand> getHands() {
		return hands;
	}

	public boolean containsId(Type type, int id) {
		switch (type) {
		case HAND:
			return handIds.contains(id);
		case SKELETON:
			return this.id == id;
		default:
			throw new RuntimeException("Invalid type");
		}
	}

	public Integer getId() {
		return id;
	}
	
	public static class SkeletonBuilder {
		private List<HandBuilder> handBuilders = new ArrayList<>();
		private boolean hasLeft = false;
		private int id;

		public SkeletonBuilder() {
			this(Optional.empty());
		}
		
		public SkeletonBuilder(Optional<Integer> id) {
			if(id.isPresent()) {
				this.id = id.get();
			} else {
				this.id = IdGenerator.generateId();
			}
		}
		
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
		
		public int getId() {
			return id;
		}

		public List<HandBuilder> getHandBuilders() {
			return handBuilders;
		}
	}

}
