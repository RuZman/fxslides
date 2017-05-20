package de.ruzman.hui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Vector;

import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.hui.skeleton.Hand.HandBuilder;
import de.ruzman.hui.skeleton.Point;
import de.ruzman.hui.skeleton.Skeleton;
import de.ruzman.hui.skeleton.Skeleton.SkeletonBuilder;
import de.ruzman.hui.skeleton.Skeleton.Type;
import de.ruzman.hui.skeleton.World;
import de.ruzman.leap.LeapApp;
import de.ruzman.leap.event.LeapEvent;
import de.ruzman.leap.event.LeapEventHandler;
import de.ruzman.leap.event.LeapListener;

public class SkeletonRegistry implements LeapListener {
	private List<SkeletonListener> skeletonListeners = new ArrayList<>();
	private World lastWorld;

	public SkeletonRegistry() {
		LeapEventHandler.addLeapListener(this);
	}

	public void addListener(SkeletonListener skeletonListener) {
		skeletonListeners.add(skeletonListener);
	}

	@Override
	public void onFrame(Controller controller) {
		World newWorld = new World();
		addHands(newWorld, controller);
		
		if(lastWorld != null) {
			lastWorld.addMissingSkeletonParts(newWorld);
		}
		newWorld.create();
		for(Skeleton skeleton: newWorld.getSkeletons()) {
			SkeletonEvent event = new SkeletonEvent(skeleton);
			skeletonListeners.forEach(e -> e.onUpdate(event));
		}
		lastWorld = newWorld;
	}

	private void addHands(World newWorld, Controller controller) {
		Point source = new Point(null, LeapApp.getTrackingBox(), new Vector());
		for (com.leapmotion.leap.Hand hand : controller.frame().hands()) {
			Optional<SkeletonBuilder> skeletonBuilder = newWorld.containsSkeletonPart(Type.HAND, hand.id());
			
			if(!skeletonBuilder.isPresent()) {
				skeletonBuilder = Optional.of(new SkeletonBuilder());
				newWorld.addSkeletonPart(skeletonBuilder.get(), skeletonBuilder.get(), Type.SKELETON, 0);
			}
			
			Point palmPosition = new Point(source, null, hand.stabilizedPalmPosition());
			
			HandBuilder handBuilder = new HandBuilder(hand.id());
			handBuilder.palmPosition(palmPosition);
			handBuilder.hasEntered(lastWorld);
			
			skeletonBuilder.get().addHand(handBuilder);
			newWorld.addSkeletonPart(skeletonBuilder.get(), handBuilder, Type.HAND, hand.id());
		}
	}

	@Override
	public void statusChanged(LeapEvent event) {

	}
}
