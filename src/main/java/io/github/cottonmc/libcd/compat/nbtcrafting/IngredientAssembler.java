package io.github.cottonmc.libcd.compat.nbtcrafting;

import de.siphalor.nbtcrafting.ingredient.IIngredient;
import de.siphalor.nbtcrafting.ingredient.IngredientStackEntry;
import de.siphalor.nbtcrafting.util.duck.ICloneable;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeTweaker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.class_1799;
import net.minecraft.class_1856;

public class IngredientAssembler {
	public static class_1856 constructFromStacks(class_1799... stacks) {
		List<IngredientStackEntry> entries = new ArrayList<>();
		for (class_1799 stack : stacks) {
			entries.add(new IngredientStackEntry(stack));
		}
		Stream<IngredientStackEntry> entryStream = entries.stream();
		try {
			class_1856 ingredient = (class_1856)((ICloneable)(Object)class_1856.field_9017).clone();
			((IIngredient)(Object)ingredient).setAdvancedEntries(entryStream);
			return ingredient;
		} catch (CloneNotSupportedException e) {
			RecipeTweaker.INSTANCE.getLogger().error("Failed to assemble ingredient with NBT Crafting: " + e.getMessage());
			return class_1856.field_9017;
		}
	}
}
