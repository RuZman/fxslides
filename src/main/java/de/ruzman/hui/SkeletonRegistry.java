package de.ruzman.hui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Vector;

import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.skeleton.Hand;
import de.ruzman.hui.skeleton.Point;
import de.ruzman.hui.skeleton.Skeleton;
import de.ruzman.leap.LeapApp;
import de.ruzman.leap.event.LeapEvent;
import de.ruzman.leap.event.LeapEventHandler;
import de.ruzman.leap.event.LeapListener;

public class SkeletonRegistry implements LeapListener {
	private List<SkeletonListener> skeletonListeners = new ArrayList<>();
	
	private Set<Integer> handIDs = new HashSet<>();

	public SkeletonRegistry() {
		LeapEventHandler.addLeapListener(this);
	}

	public void addListener(SkeletonListener skeletonListener) {
		skeletonListeners.add(skeletonListener);
	}

	@Override
	public void onFrame(Controller controller) {
		List<Hand> hands = new ArrayList<>();
		
		Iterator<Integer> it = handIDs.iterator();
		
		while(it.hasNext()) {
			Integer handId = it.next();
			
			if(!controller.frame().hand(handId).isValid()) {
				Point source = new Point(null, LeapApp.getTrackingBox(), new Vector(), false, false);
				Point palmPosition = new Point(source, null, null, false, true);
				hands.add(new Hand(handId, palmPosition));
				it.remove();
			}
		}
		
		for (com.leapmotion.leap.Hand hand : controller.frame().hands()) {
			Point source = new Point(null, LeapApp.getTrackingBox(), new Vector(), false, false);
			Point palmPosition = new Point(source, null, hand.stabilizedPalmPosition(), !handIDs.contains(hand.id()), false);
			hands.add(new Hand(hand.id(), palmPosition));
			handIDs.add(hand.id());
		}

		Skeleton skeleton = new Skeleton.SkeletonBuilder().addHands(hands).createSkeleton();
		SkeletonEvent event = new SkeletonEvent(skeleton);
		skeletonListeners.forEach(e -> e.onUpdate(event));
	}

	@Override
	public void statusChanged(LeapEvent event) {

	}
}
