package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.CookingRecipeFactoryInvoker;
import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_1874;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.recipe.CookingRecipeSerializer$RecipeFactory")
public interface MixinCookingRecipeFactory<T extends class_1874> extends CookingRecipeFactoryInvoker {
	@Shadow T create(class_2960 identifier, String s, class_1856 ingredient, class_1799 itemStack, float v, int i);

	default T libcd_create(class_2960 id, String group, class_1856 input, class_1799 output, float experience, int cookingTime) {
		return create(id, group, input, output, experience, cookingTime);
	}
}
