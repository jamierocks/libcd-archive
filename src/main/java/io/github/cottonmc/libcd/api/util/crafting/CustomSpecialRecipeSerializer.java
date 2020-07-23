package io.github.cottonmc.libcd.api.util.crafting;

import io.github.cottonmc.libcd.api.tweaker.recipe.CustomSpecialCraftingRecipe;
import net.minecraft.class_1866;

public class CustomSpecialRecipeSerializer extends class_1866<CustomSpecialCraftingRecipe> {
	public static final CustomSpecialRecipeSerializer INSTANCE = new CustomSpecialRecipeSerializer();

	private CustomSpecialRecipeSerializer() {
		super(CustomSpecialCraftingRecipe::new);
	}
}
