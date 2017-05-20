package de.ruzman.hui.event;

import de.ruzman.hui.skeleton.Skeleton;

public class SkeletonEvent {
	private Skeleton skeleton;
	
	public SkeletonEvent(Skeleton skeleton) {
		this.skeleton = skeleton;
	}
	
	public Skeleton getSkeleton() {
		return skeleton;
	}
}
