package de.ruzman.hui;

import java.util.ArrayList;
import java.util.List;

import de.ruzman.hui.device.DataProvider;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.gesture.GestureProvider;
import de.ruzman.hui.skeleton.Skeleton;
import de.ruzman.hui.skeleton.World;

public class SkeletonRegistry {
	private List<SkeletonListener> skeletonListeners = new ArrayList<>();
	private List<DataProvider> dataProviders = new ArrayList<>();
	private List<GestureProvider> gestureProviders = new ArrayList<>();
	private World lastWorld = new World();

	public SkeletonRegistry() {
	}

	public void addListener(SkeletonListener skeletonListener) {
		skeletonListeners.add(skeletonListener);
	}

	public void removeListener(SkeletonListener listener) {
		skeletonListeners.remove(listener);
	}

	public void addGestureProvider(GestureProvider gestureProvider) {
		gestureProviders.add(gestureProvider);
	}
	
	public void addDataProvider(DataProvider dataProvider) {
		dataProviders.add(dataProvider);
	}
	
	public void update() {
		World newWorld = new World();
		
		for(DataProvider dataProvider: dataProviders) {			
			dataProvider.addSkeleton(newWorld, lastWorld);
			dataProvider.addHands(newWorld, lastWorld);
			dataProvider.addFingers(newWorld, lastWorld);
		}
		
		lastWorld.addMissingSkeletonParts(newWorld);
		newWorld.create();
		
		for(Skeleton skeleton: newWorld.getSkeletons()) {
			List<String> gestures = new ArrayList<>();
			for(GestureProvider gestureProvider: gestureProviders) {
				gestures.addAll(gestureProvider.provideGesture(skeleton));
			}
			
			SkeletonEvent event = new SkeletonEvent(skeleton, gestures);
			skeletonListeners.forEach(e -> e.onUpdate(event));
			skeletonListeners.stream()
				.filter(e -> !gestures.isEmpty())
				.forEach(listener -> listener.onGesture(event));
		}
		lastWorld = newWorld;
	}
}
