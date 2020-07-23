package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.CraftingResultSlotAccessor;
import net.minecraft.class_1657;
import net.minecraft.class_1734;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_1734.class)
public class MixinCraftingResultSlot implements CraftingResultSlotAccessor {
	@Shadow @Final private class_1657 player;

	@Override
	public class_1657 libcd$getPlayer() {
		return player;
	}
}
