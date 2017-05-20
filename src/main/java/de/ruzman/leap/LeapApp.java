package de.ruzman.leap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import static de.ruzman.newfx.event.CursorEvent.ANY;

import com.leapmotion.leap.Controller;

import de.ruzman.leap.event.LeapEventHandler;
import de.ruzman.leap.fx.LeapStageDecorator;
import de.ruzman.newfx.control.CursorNodeFactory;
import de.ruzman.newfx.event.CursorEvent;

/**
 * LeapApp contains the configuration of a Leap Motion project. This class
 * follows the singleton pattern and cannot be initialized twice. Create an
 * instance with {@link LeapAppBuilder}.
 */
public final class LeapApp {
	private static LeapApp instance;

	private MotionRegistry motionRegistry;
	private TrackingBox trackingBox;
	private int minimumHandNumber = 1;
	private int maximumHandNumber = Integer.MAX_VALUE;
	private double trackedAreaWidth;
	private double trackedAreaHeight;
	private boolean usePolling;
	private boolean stopPollingOnFocusLost;
	private LeapStageDecorator leapStageDecorator;

	private Controller controller;

	private LeapApp(Controller controller) {
		this.controller = controller;
	}

	private void init(TrackingBox trackingBox, int minimumHandNumber, int maximumHandNumber, boolean usePolling,
			boolean stopPollingOnFocusLost, MotionRegistry motionRegistry, Stage stage, CursorNodeFactory cursorNodeFactory, boolean shouldDecorateStage) {

		// FIXME: Beim doppelten Aufruf ist das nicht mehr korrekt.
		this.motionRegistry = motionRegistry;
		LeapEventHandler.addLeapListener(motionRegistry);

		if (stage != null) {
			stage.widthProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					trackedAreaWidth = newValue.doubleValue();
				}
			});
			stage.heightProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					trackedAreaHeight = newValue.doubleValue();
				}
			});
			synchronizeWithLeapMotion();

			if (shouldDecorateStage) {				
				leapStageDecorator = new LeapStageDecorator(stage, cursorNodeFactory);
			}
		}

		this.trackingBox = trackingBox;

		setMinimumHandNumber(minimumHandNumber);
		setMaximumHandNumber(maximumHandNumber);

		this.usePolling = usePolling;
		this.stopPollingOnFocusLost = stopPollingOnFocusLost;

		// FIXME: Beim doppelten Aufruf ist das nicht mehr korrekt.
		if (!usePolling) {
			controller.addListener(LeapEventHandler.getInstance());
		}
	}

	private void synchronizeWithLeapMotion() {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0 / 60.0), ea -> LeapApp.update()));
		timeline.play();
	}

	public static TrackingBox getTrackingBox() {
		return instance.trackingBox;
	}

	private static void setMinimumHandNumber(int minimumHandNumber) {
		validateHandNumber();
		instance.minimumHandNumber = minimumHandNumber;
	}
	

	public static void setTrackingBox(TrackingBox trackingBox2) {
		instance.trackingBox = trackingBox2;
	}

	public static int getMinimumHandNumber() {
		return instance.minimumHandNumber;
	}

	private static void setMaximumHandNumber(int maximumHandNumber) {
		validateHandNumber();
		instance.maximumHandNumber = maximumHandNumber;
	}

	public static int getMaximumHandNumber() {
		return instance.maximumHandNumber;
	}

	private static void validateHandNumber() {
		if (instance.maximumHandNumber < instance.minimumHandNumber) {
			throw new IllegalArgumentException("MaximumHandNumber must be >= minumumHandNumber");
		}

		if (instance.maximumHandNumber < 1) {
			throw new IllegalArgumentException("MinimumHandNumber must be >= 1");
		}

		if (instance.minimumHandNumber < 1) {
			throw new IllegalArgumentException("MinimumHandNumber must be >= 1");
		}
	}

	public static double getTrackedAreaWidth() {
		return instance.trackedAreaWidth;
	}

	public static double getTrackedAreaHeight() {
		return instance.trackedAreaHeight;
	}

	public static MotionRegistry getMotionRegistry() {
		return instance.motionRegistry;
	}

	public static Controller getController() {
		return instance.controller;
	}
	
	public static void setCursorNodeFactory(CursorNodeFactory cursorNodeFactory) {
		instance.leapStageDecorator.setCursorNodeFactory(cursorNodeFactory);
	}

	public static void update() {
		if (instance.usePolling) {
			LeapEventHandler.update();
		}
	}

	public static void destroy() {
		LeapEventHandler.removeAllLeapListener();
		instance.controller.delete();
		try {
			instance.finalize();
		} catch (Throwable t) {
			// Do nothing.
		} finally {
			instance = null;
			System.exit(0);
		}
	}

	public static boolean stopPollingOnFocusLost() {
		return instance.stopPollingOnFocusLost;
	}

	/**
	 * LeapAppBuilder initialize a LeapApp instance. Default configuration:
	 * <ul>
	 * <li>Load native libraries from: "native"
	 * <li>TrackingBox: use predefined user settings (InteractionBox)
	 * <li>Minimum hand number: 1
	 * <li>Maximum hand number: unlimited
	 * <li>Display width: width of the default screen
	 * <li>Display height: height of the default screen
	 * <li>Polling: activated
	 * <li>motionRegistry: default
	 * </ul>
	 * <p>
	 * Short version: <code>new LeapAppBuilder().initLeapApp();</code>
	 * <p>
	 * Same as:
	 * 
	 * <pre>
	 * <code>new LeapAppBuilder(true, "native")
	 * 	.trackingBox(new TrackingBox())
	 * 	.minimumHandNumber(1)
	 * 	.maximumHandNumber(Integer.MAX_VALUE)
	 * 	.displayWidth(dispMode.getWidth())
	 * 	.displayHeight(dispMode.getHeight())
	 * 	.usePolling(true)
	 * 	.motionRegistry(new MotionRegistry());
	 * 	.initLeapApp();
	 * </code>
	 * </pre>
	 */
	public static class LeapAppBuilder {
		private TrackingBox trackingBox;
		private int minimumHandNumber = 1;
		private int maximumHandNumber = Integer.MAX_VALUE;
		private boolean usePolling = true;
		private boolean stopPollingOnFocusLost = true;
		private MotionRegistry motionRegistry;
		private Stage stage;
		private boolean shouldDecorateStage = true;
		private CursorNodeFactory cursorNodeFactory = new CursorNodeFactory() {

			@Override
			public Circle createCursor() {
				return new Circle(0, 0, 18, Color.rgb(240, 240, 240));
			}
		};


		/**
		 * See: {@link LeapAppBuilder#LeapAppBuilder(boolean)
		 * LeapAppBuilder(stage, true)}.
		 */
		public LeapAppBuilder(Stage stage) {
			this(stage, true);
		}

		/**
		 * See: {@link LeapAppBuilder#LeapAppBuilder(boolean, String)
		 * LeapAppBuilder(stage, shouldLoadNativeLibraries, "native")}.
		 * 
		 * @param shouldLoadNativeLibraries
		 *            Whether the LeapAppBuilder should load native libraries.
		 */
		public LeapAppBuilder(Stage stage, boolean shouldLoadNativeLibraries) {
			this(stage, shouldLoadNativeLibraries, "native");
		}

		/**
		 * Loads native libraries if
		 * <code>shouldLoadNativeLibraries} == true</code> and initializes this
		 * Leap Motion framework.
		 * 
		 * @param shouldLoadNativeLibraries
		 *            Whether the LeapAppBuilder should load native libraries.
		 * @param path
		 *            Path of the native libraries.
		 */
		public LeapAppBuilder(Stage stage, boolean shouldLoadNativeLibraries, String path) {
			if (shouldLoadNativeLibraries) {
				NativeLibrary.loadSystem(path);
			}

			if (instance == null) {
				instance = new LeapApp(new Controller());
			}
			trackingBox = new TrackingBox();
			motionRegistry = new MotionRegistry();
			this.stage = stage;
		}

		/**
		 * Initializes this Leap Motion framework with the given configuration.
		 * 
		 * @return LeapApp
		 */
		public LeapApp initLeapApp() {
			instance.init(trackingBox, minimumHandNumber, maximumHandNumber, usePolling, stopPollingOnFocusLost,
					motionRegistry, stage, cursorNodeFactory, shouldDecorateStage);

			return instance;
		}

		/**
		 * Use a specified {@link TrackingBox}, within which the hands are
		 * tracked. Default value: <code>new TrackingBox()</code>.
		 * 
		 * @param trackingBox
		 *            TrackingBox
		 * @return LeapAppBuilder
		 */
		public LeapAppBuilder trackingBox(TrackingBox trackingBox) {
			this.trackingBox = trackingBox;
			return this;
		}

		/**
		 * Set the minimum number of hands. Default value: <code>1</code>.
		 * 
		 * @param minimumHandNumber
		 *            Minimum number of hands
		 * @return LeapAppBuilder
		 */
		public LeapAppBuilder minimumHandNumber(int minimumHandNumber) {
			this.minimumHandNumber = minimumHandNumber;
			return this;
		}

		/**
		 * Set the maximum number of hands. Default value:
		 * <code>Integer.MAX_VALUE</code>.
		 * 
		 * @param maximumHandNumber
		 *            Maximum number of hands
		 * @return LeapAppBuilder
		 */
		public LeapAppBuilder maximumHandNumber(int maximumHandNumber) {
			this.maximumHandNumber = maximumHandNumber;
			return this;
		}

		/**
		 * Whether the framework-logic should get the data by polling. Default
		 * value: <code>true</code>
		 * 
		 * @param usePolling
		 *            <code>true</code> if the framework-logic should get the
		 *            data by polling.
		 * @return LeapAppBuilder
		 */
		public LeapAppBuilder usePolling(boolean usePolling) {
			return usePolling(usePolling, true);
		}

		public LeapAppBuilder usePolling(boolean usePolling, boolean stopPollingOnFocusLost) {
			this.usePolling = usePolling;
			this.stopPollingOnFocusLost = stopPollingOnFocusLost;
			return this;
		}

		public LeapAppBuilder decorateStage(boolean shouldDecorateStage) {
			this.shouldDecorateStage = shouldDecorateStage;
			return this;
		}
		
		public LeapAppBuilder cursorNodeFactory(CursorNodeFactory cursorNodeFactory) {
			this.cursorNodeFactory = cursorNodeFactory;
			return this;
		}

		/**
		 * Set the {@link MotionRegistry}.
		 * 
		 * @param motionRegistry
		 *            MotionRegistry
		 * @return LeapAppBuilder
		 */
		public LeapAppBuilder motionRegistry(MotionRegistry motionRegistry) {
			this.motionRegistry = motionRegistry;
			return this;
		}
	}
}