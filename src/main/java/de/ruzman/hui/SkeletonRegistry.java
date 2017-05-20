package de.ruzman.hui;

import java.util.ArrayList;
import java.util.List;

import com.leapmotion.leap.Controller;

import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.skeleton.Skeleton;
import de.ruzman.leap.event.LeapEvent;
import de.ruzman.leap.event.LeapEventHandler;
import de.ruzman.leap.event.LeapListener;

public class SkeletonRegistry implements LeapListener {
	private List<SkeletonListener> skeletonListeners = new ArrayList<>();
	
	public SkeletonRegistry() {
		LeapEventHandler.addLeapListener(this);
	}
	
	public void addListener(SkeletonListener skeletonListener) {
		skeletonListeners.add(skeletonListener);
	}

	@Override
	public void onFrame(Controller controller) {
		Skeleton skeleton = new Skeleton();
		SkeletonEvent event = new SkeletonEvent(skeleton);
		skeletonListeners.forEach(e -> e.onUpdate(event));
	}

	@Override
	public void statusChanged(LeapEvent event) {
		
	}
}
