package me.client.Managers;

import java.util.List;

/*
 * Be sure when you're setting up ListManager to set contents to something! YOU WON'T ENJOY IT WHEN YOU DON'T!
 */
public abstract class ListManager<T> {
	protected List<T> contents;

	public final List<T> getContents() {
		return contents;
	}

	public abstract void setup();
}
