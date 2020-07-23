package io.github.cottonmc.libcd.impl;

import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_1874;
import net.minecraft.class_2960;

public interface CookingRecipeFactoryInvoker<T extends class_1874> {
	T libcd$create(class_2960 id, String group, class_1856 ingredient, class_1799 output, float experience, int cookingTime);
}
