package de.ruzman.hui;

import java.util.ArrayList;
import java.util.List;

import de.ruzman.hui.device.DataProvider;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.skeleton.Skeleton;
import de.ruzman.hui.skeleton.World;

public class SkeletonRegistry {
	private List<SkeletonListener> skeletonListeners = new ArrayList<>();
	private List<DataProvider> dataProviders = new ArrayList<>();
	private World lastWorld = new World();

	public SkeletonRegistry() {
	}

	public void addListener(SkeletonListener skeletonListener) {
		skeletonListeners.add(skeletonListener);
	}
	
	public void addDataProvider(DataProvider dataProvider) {
		dataProviders.add(dataProvider);
	}
	
	public void update() {
		World newWorld = new World();
		
		for(DataProvider dataProvider: dataProviders) {
			dataProvider.addHands(newWorld, lastWorld);
			dataProvider.addFingers(newWorld, lastWorld);
		}
		
		lastWorld.addMissingSkeletonParts(newWorld);
		newWorld.create();
		for(Skeleton skeleton: newWorld.getSkeletons()) {
			SkeletonEvent event = new SkeletonEvent(skeleton);
			skeletonListeners.forEach(e -> e.onUpdate(event));
		}

		lastWorld = newWorld;
	}
}
