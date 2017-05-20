package de.ruzman.hui.skeleton;

import java.util.Optional;

public class Hand {
	private long id;
	private Optional<Point> palmPosition;
	
	public Hand(long id, Point palmPosition) {
		this.id = id;
		this.palmPosition = Optional.ofNullable(palmPosition);
	}
	
	public Optional<Point> getPalmPosition() {
		return palmPosition;
	}
	
	public long getId() {
		return id;
	}
}
