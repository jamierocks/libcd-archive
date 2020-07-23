package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.CraftingInventoryAccessor;
import net.minecraft.class_1703;
import net.minecraft.class_1715;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_1715.class)
public class MixinCraftingInventory implements CraftingInventoryAccessor {
	@Shadow @Final private class_1703 handler;

	@Override
	public class_1703 libcd$getHandler() {
		return handler;
	}
}
