package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.CuttingRecipeFactoryInvoker;
import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_2960;
import net.minecraft.class_3972;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.recipe.CuttingRecipe$Serializer$RecipeFactory")
public interface MixinCuttingRecipeFactory<T extends class_3972> extends CuttingRecipeFactoryInvoker {
	@Shadow T create(class_2960 identifier, String s, class_1856 ingredient, class_1799 itemStack);

	default T libcd$create(class_2960 id, String group, class_1856 input, class_1799 output) {
		return create(id, group, input, output);
	}
}
