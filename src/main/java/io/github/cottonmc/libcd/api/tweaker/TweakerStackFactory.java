package io.github.cottonmc.libcd.api.tweaker;

import net.minecraft.class_1799;
import net.minecraft.class_2960;

/**
 * Some ItemStacks, like vanilla potions, have entirely different functions and names based on NBT.
 * Because of that, it's hard to use those stacks in recipes.
 * CottonTweaker uses a "[getter id]->[entry id]" syntax to get those recipes
 */
public interface TweakerStackFactory {
	/**
	 * Get an ItemStack from a registered processor
	 * @param entry The Identifier of the entry to get
	 * @return the proper ItemStack for the given Identifier, or an empty stack if the entry doesn't exist
	 */
	class_1799 getSpecialStack(class_2960 entry);
}
