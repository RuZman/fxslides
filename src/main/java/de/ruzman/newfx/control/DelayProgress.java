package de.ruzman.newfx.control;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class DelayProgress extends Region {
	private AnimationTimer timer;
	private double progress;
	private int delay;
	private Arc arc;

	public DelayProgress(final int delay, final double centerX, final double centerY, double startAngle,
			double outerRadius, Color fill) {
		super();

		this.delay = delay;
		arc = new Arc();
		arc.setStartAngle(startAngle);
		arc.setCenterX(centerX);
		arc.setCenterY(centerY);
		arc.setRadiusX(outerRadius);
		arc.setRadiusY(outerRadius);
		arc.setFill(fill);
		arc.setType(ArcType.ROUND);

		getChildren().add(arc);
		reset();
	}

	public void reset() {
		progress = 0;

		arc.setLength(0);

		if (timer != null) {
			timer.stop();
		}
	}

	public void start(Runnable onFinish) {
		timer = new AnimationTimer() {
			private long lasttime = 0;
			private int ms;

			@Override
			public void handle(long now) {
				if (progress >= 1.0f) {
					onFinish.run();
					progress = 1.00f;
					timer.stop();
				} else if (now > lasttime + 100000000 / 2) {
					ms += 5;
					progress = (float) ms / delay;
					lasttime = now;
					arc.setLength(-progress * 360);
				}
			}
		};
		timer.start();
	}
}
