package io.github.cottonmc.libcd.api.util;

import net.minecraft.class_1937;

/**
 * A wrapped view of a world, accessible outside of obfuscation.
 */
public class WorldInfo {
	private class_1937 world;

	public WorldInfo(class_1937 world) {
		this.world = world;
	}

	/**
	 * @return Whether it's currently daytime or not.
	 */
	public boolean isDay() {
		return world.method_8530();
	}

	/**
	 * @return The current time of day - from 0 to 23000.
	 */
	public long getTime() {
		return world.method_8532();
	}

	/**
	 * @return Whether the world itself is currently raining.
	 */
	public boolean isRaining() {
		return world.method_8419();
	}

	/**
	 * @return Whether the world itself is currently thundering.
	 */
	public boolean isThundering() {
		return world.method_8546();
	}

	/**
	 * @return The int value of the global difficulty of the world.
	 */
	public int getDifficulty() {
		return world.method_8401().method_207().method_5461();
	}
}
