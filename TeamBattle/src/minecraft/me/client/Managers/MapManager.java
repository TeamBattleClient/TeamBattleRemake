package me.client.Managers;

import java.util.Map;

/*
 * Be sure when you're setting up MapManager to set contents to something! YOU WON'T ENJOY IT WHEN YOU DON'T!
 */
public abstract class MapManager<T, U> {
	protected Map<T, U> contents;

	public final Map<T, U> getContents() {
		return contents;
	}

	public abstract void setup();
}
