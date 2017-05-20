package de.ruzman.hui.skeleton;

import java.util.Optional;

public class Finger {
	private int id;
	private Optional<Point> tipPosition;
	
	public Finger(int id, Point tipPosition) {
		this.id = id;
		this.tipPosition = Optional.ofNullable(tipPosition);
	}
}
