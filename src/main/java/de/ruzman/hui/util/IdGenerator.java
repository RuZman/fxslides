package de.ruzman.hui.util;

public final class IdGenerator {
	private static int lastId = 10000;
	
	public static synchronized int generateId() {
		return lastId++;
	}
}
