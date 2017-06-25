package de.ruzman.hui.device;

import java.util.Optional;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Vector;

import de.ruzman.hui.skeleton.Hand.HandBuilder;
import de.ruzman.hui.skeleton.Skeleton.SkeletonBuilder;
import de.ruzman.hui.skeleton.SkeletonPart.SkeletonPartBuilder;
import de.ruzman.common.Point;
import de.ruzman.hui.skeleton.World;
import de.ruzman.hui.skeleton.Finger.FingerBuilder;
import de.ruzman.hui.skeleton.FingerType;
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

	@Override
	public void onFrame(Controller controller) {
		frame = controller.frame();
	}

	@Override
	public void addSkeleton(World newWorld, World lastWorld) {
	}

	@Override
	public void addHands(World newWorld, World lastWorld) {
		for (com.leapmotion.leap.Hand hand : frame.hands()) {
			Point palmPosition = new Point(source, null, hand.stabilizedPalmPosition());
			HandBuilder lastHandBuilder = lastWorld.getHandBuilder(hand.id()).orElse(null);
			HandBuilder handBuilder = new HandBuilder(hand.id(), lastHandBuilder);
			handBuilder.palmPosition(palmPosition);

			SkeletonBuilder skeletonBuilder = findOrCreateSkeleton(newWorld, lastWorld, handBuilder);
			skeletonBuilder.addHand(handBuilder);
			newWorld.addSkeletonPart(skeletonBuilder, handBuilder);
		}
	}

	@Override
	public void addFingers(World newWorld, World lastWorld) {
		for (com.leapmotion.leap.Finger finger : frame.fingers()) {
			FingerBuilder lastFingerBuilder = lastWorld.getFingerBuilder(finger.id()).orElse(null);
			FingerType fingerType = null;
			switch(finger.type()) {
				case TYPE_INDEX: fingerType = FingerType.INDEX; break;
				case TYPE_MIDDLE: fingerType = FingerType.MIDDLE; break;
				case TYPE_PINKY: fingerType = FingerType.PINKY; break;
				case TYPE_RING: fingerType = FingerType.RING; break;
				case TYPE_THUMB: fingerType = FingerType.THUMB; break;
			}
			Point tipPosition = new Point(source, null, finger.stabilizedTipPosition());
			
			FingerBuilder fingerBuilder = new FingerBuilder(finger.id(), fingerType, lastFingerBuilder);
			fingerBuilder.tipPosition(tipPosition);

			HandBuilder handBuilder = newWorld.getHandBuilder(finger.hand().id()).get();
			handBuilder.addFinger(fingerBuilder);

			newWorld.addSkeletonPart(newWorld.containsSkeletonPart(handBuilder).get(), fingerBuilder);
		}
	}

	@Override
	public void statusChanged(LeapEvent event) {

	}

	private SkeletonBuilder findOrCreateSkeleton(World newWorld, World lastWorld,
			SkeletonPartBuilder<?, ?> partBuilder) {
		Optional<SkeletonBuilder> lastSkeletonBuilder = lastWorld.containsSkeletonPart(partBuilder);
		Optional<SkeletonBuilder> skeletonBuilder = newWorld.containsSkeletonPart(partBuilder);

		if (!lastSkeletonBuilder.isPresent()) {
			skeletonBuilder = Optional.of(new SkeletonBuilder());
			newWorld.addSkeletonPart(skeletonBuilder.get(), skeletonBuilder.get());
		} else {
			skeletonBuilder = Optional.of(new SkeletonBuilder(Optional.of(lastSkeletonBuilder.get().getId()),
					lastSkeletonBuilder.get().getInitializedObject()));
			newWorld.addSkeletonPart(skeletonBuilder.get(), skeletonBuilder.get());
		}

		return skeletonBuilder.get();
	}
}
