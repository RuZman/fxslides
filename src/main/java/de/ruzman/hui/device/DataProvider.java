package de.ruzman.hui.device;

import de.ruzman.hui.skeleton.World;

public interface DataProvider {
	public void addSkeleton(World newWorld, World lastWorld);
	public void addHands(World newWorld, World lastWorld);
	public void addFingers(World newWorld, World lastWorld);
}
