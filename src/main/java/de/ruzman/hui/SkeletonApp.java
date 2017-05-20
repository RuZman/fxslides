package de.ruzman.hui;

import java.util.Optional;

import de.ruzman.hui.event.SkeletonListener;
import de.ruzman.leap.LeapApp.LeapAppBuilder;
import de.ruzman.leap.event.LeapEventHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public final class SkeletonApp {	
	private boolean isLeapMotionActive;
	private static SkeletonRegistry skeletonRegistry;
	
	private SkeletonApp(Optional<LeapAppBuilder> leapAppBuilder) {
		skeletonRegistry = new SkeletonRegistry();
		
		if(leapAppBuilder.isPresent()) {
			leapAppBuilder.get().initLeapApp();
			isLeapMotionActive = true;
		}
		
		usePolling();
	}
	
	public static void addListener(SkeletonListener listener) {
		skeletonRegistry.addListener(listener);
	}
	
	private void usePolling() {
		if(isLeapMotionActive) {
			Timeline timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0 / 60.0), ea -> LeapEventHandler.update()));
			timeline.play();
		}
	}
	
	public static class SkeletonAppBuilder {
		private Optional<LeapAppBuilder> leapAppBuilder = Optional.empty();
		
		public SkeletonApp createHuiApp() {
			return new SkeletonApp(leapAppBuilder);
		}
		
		public SkeletonAppBuilder initLeapMotion(LeapAppBuilder leapAppBuilder) {
			this.leapAppBuilder = Optional.of(leapAppBuilder);
			return this;
		}
	}
}
