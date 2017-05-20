package de.ruzman.hui.skeleton;

import java.util.Collections;
import java.util.List;

public class Skeleton {
	private List<Hand> hands;
	
	private Skeleton(SkeletonBuilder skeletonBuilder) {
		hands = Collections.unmodifiableList(skeletonBuilder.hands);
	}

	public List<Hand> getHands() {
		return hands;
	}

	public static class SkeletonBuilder {
		protected List<Hand> hands;
		
		public SkeletonBuilder addHands(List<Hand> hands) {
			this.hands = hands;
			return this;
		}
		
		public Skeleton createSkeleton() {
			return new Skeleton(this);
		}
	}
}
