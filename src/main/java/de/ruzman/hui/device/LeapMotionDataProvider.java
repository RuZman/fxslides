package de.ruzman.hui.device;

import java.util.Optional;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Vector;

import de.ruzman.hui.skeleton.Hand.HandBuilder;
import de.ruzman.hui.skeleton.Point;
import de.ruzman.hui.skeleton.Skeleton.SkeletonBuilder;
import de.ruzman.hui.skeleton.Skeleton.Type;
import de.ruzman.hui.skeleton.World;
import de.ruzman.leap.LeapApp;
import de.ruzman.leap.event.LeapEvent;
import de.ruzman.leap.event.LeapEventHandler;
import de.ruzman.leap.event.LeapListener;

public class LeapMotionDataProvider implements LeapListener, DataProvider {
	private Frame frame;
	private Point source;

	public LeapMotionDataProvider() {
		LeapEventHandler.addLeapListener(this);
		source = new Point(null, LeapApp.getTrackingBox(), new Vector());
	}

	public void addHands(World newWorld, World lastWorld) {
		for (com.leapmotion.leap.Hand hand : frame.hands()) {
			SkeletonBuilder skeletonBuilder = findOrCreateSkeleton(newWorld, lastWorld, Type.HAND, hand.id());
			Point palmPosition = new Point(source, null, hand.stabilizedPalmPosition());

			HandBuilder handBuilder = new HandBuilder(hand.id());
			handBuilder.palmPosition(palmPosition);
			handBuilder.hasEntered(lastWorld);

			skeletonBuilder.addHand(handBuilder);
			newWorld.addSkeletonPart(skeletonBuilder, handBuilder, Type.HAND, hand.id());
		}
	}

	@Override
	public void onFrame(Controller controller) {
		frame = controller.frame();
	}

	@Override
	public void statusChanged(LeapEvent event) {

	}

	@Override
	public void addFingers(World newWorld, World lastWorld) {
		for (com.leapmotion.leap.Finger finger : frame.fingers()) {
			HandBuilder handBuilder = newWorld.getHandBuilder(finger.hand().id()).get();
		}
	}

	private SkeletonBuilder findOrCreateSkeleton(World newWorld, World lastWorld, Type type, int id) {
		Optional<SkeletonBuilder> lastSkeletonBuilder = lastWorld.containsSkeletonPart(type, id);
		Optional<SkeletonBuilder> skeletonBuilder = newWorld.containsSkeletonPart(type, id);

		if (!lastSkeletonBuilder.isPresent()) {
			skeletonBuilder = Optional.of(new SkeletonBuilder());
			newWorld.addSkeletonPart(skeletonBuilder.get(), skeletonBuilder.get(), Type.SKELETON,
					skeletonBuilder.get().getId());
		} else {
			skeletonBuilder = Optional.of(new SkeletonBuilder(Optional.of(lastSkeletonBuilder.get().getId())));
			newWorld.addSkeletonPart(skeletonBuilder.get(), skeletonBuilder.get(), Type.SKELETON,
					skeletonBuilder.get().getId());
		}

		return skeletonBuilder.get();
	}
}
