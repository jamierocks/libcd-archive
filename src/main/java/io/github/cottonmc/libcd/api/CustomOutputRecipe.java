package io.github.cottonmc.libcd.api;

import java.util.Collection;
import net.minecraft.class_1792;

/**
 * A recipe that has output behavior that cannot be described by just the Recipe#getOutput() method.
 * Used for RecipeTweaker remove-by-output code.
 */
public interface CustomOutputRecipe {
	Collection<class_1792> getOutputItems();
}
