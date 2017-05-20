package de.ruzman.hui.device;

import de.ruzman.hui.skeleton.World;

public interface DataProvider {
	public void init();
	public void loadData();
	public void addHands(World newWorld, World lastWorld);
}
