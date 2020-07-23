package io.github.cottonmc.libcd.api.util;

import io.github.cottonmc.libcd.api.util.crafting.CraftingDamageSource;
import net.minecraft.class_1657;

/**
 * A wrapper on player entities so they can be manipulated outside of obfuscation
 */
public class WrappedPlayer {
	private class_1657 player;

	public WrappedPlayer(class_1657 player) {
		this.player = player;
	}

	/**
	 * @return Whether the player is real or not.
	 */
	public boolean exists() {
		return true;
	}

	/**
	 * @return The current health of the player.
	 */
	public float getHealth() {
		return player.method_6032();
	}

	/**
	 * @return The current food level of the player.
	 */
	public int getFood() {
		return player.method_7344().method_7586();
	}

	/**
	 * @return The current saturation level of the player.
	 */
	public float getSaturation() {
		return player.method_7344().method_7589();
	}

	/**
	 * @return The current food level plus saturation level of the player.
	 */
	public float getTotalHunger() {
		return player.method_7344().method_7586() + player.method_7344().method_7589();
	}

	/**
	 * @return The current experience level of the player.
	 */
	public int getLevel() {
		return player.field_7520;
	}

	/**
	 * @return Whether the player is currently wet for Riptide purposes - in water or rain.
	 */
	public boolean isWet() {
		return player.method_5721();
	}

	/**
	 * @return Whether the player is in creative mode.
	 */
	public boolean isCreative() {
		return player.method_7337();
	}

	/**
	 * Damage the player.
	 * @param amount The amount of hearts to take.
	 */
	public boolean damage(float amount) {
		if (!player.field_6002.field_9236) return player.method_5643(CraftingDamageSource.INSTANCE, amount);
		return false;
	}

	public boolean damage(int amount) {
		if (!player.field_6002.field_9236) return player.method_5643(CraftingDamageSource.INSTANCE, (float)amount);
		return false;
	}

	/**
	 * Take food levels or saturation from the player.
	 * @param amount The number of levels to take.
	 */
	public void takeFood(int amount) {
		if (!player.field_6002.field_9236) player.method_7344().method_7583(amount * 10);
	}

	/**
	 * Take experience levels from the player.
	 * @param amount The amount of levels to take.
	 */
	public void takeLevels(int amount) {
		if (!player.field_6002.field_9236) player.field_7520 -= amount;
	}
}
