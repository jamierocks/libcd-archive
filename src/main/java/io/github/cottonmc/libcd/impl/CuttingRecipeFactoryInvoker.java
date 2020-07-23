package io.github.cottonmc.libcd.impl;

import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_2960;
import net.minecraft.class_3972;

public interface CuttingRecipeFactoryInvoker<T extends class_3972> {
	T libcd_create(class_2960 id, String group, class_1856 input, class_1799 output);
}
