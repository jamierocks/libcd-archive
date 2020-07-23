package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.RecipeMapAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import net.minecraft.class_1860;
import net.minecraft.class_1863;
import net.minecraft.class_2960;
import net.minecraft.class_3956;

@Mixin(class_1863.class)
public abstract class MixinRecipeManager implements RecipeMapAccessor {
	@Shadow
	@Final
	private Map<class_3956<?>, Map<class_2960, class_1860<?>>> recipeMap;

	@Override
	public Map<class_3956<?>, Map<class_2960, class_1860<?>>> libcd_getRecipeMap() {
		return recipeMap;
	}

}
