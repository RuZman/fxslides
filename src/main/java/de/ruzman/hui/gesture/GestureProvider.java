package de.ruzman.hui.gesture;

import java.util.List;

import de.ruzman.hui.skeleton.Skeleton;

public interface GestureProvider {
	public List<String> provideGesture(Skeleton skeleton);
}
