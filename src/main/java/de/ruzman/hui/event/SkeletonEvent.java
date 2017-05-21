package de.ruzman.hui.event;

import java.util.List;

import de.ruzman.hui.skeleton.Skeleton;

public class SkeletonEvent {
	private Skeleton skeleton;
	private List<String> gestures;
	
	public SkeletonEvent(Skeleton skeleton, List<String> gestures) {
		this.skeleton = skeleton;
		this.gestures = gestures;
	}
	
	public List<String> getGestures() {
		return gestures;
	}
	
	public Skeleton getSkeleton() {
		return skeleton;
	}
}
