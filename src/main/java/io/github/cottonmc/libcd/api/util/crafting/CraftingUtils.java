package io.github.cottonmc.libcd.api.util.crafting;

import io.github.cottonmc.libcd.api.util.StackInfo;
import io.github.cottonmc.libcd.impl.CraftingInventoryAccessor;
import io.github.cottonmc.libcd.impl.CraftingResultSlotAccessor;
import io.github.cottonmc.libcd.impl.PlayerScreenHandlerAccessor;
import javax.annotation.Nullable;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1714;
import net.minecraft.class_1715;
import net.minecraft.class_1723;

public class CraftingUtils {
	@Nullable
	public static class_1657 findPlayer(class_1715 inventory) {
		try {
			class_1703 container = ((CraftingInventoryAccessor) inventory).libcd$getHandler();
			if (container instanceof class_1723) {
				return ((PlayerScreenHandlerAccessor) container).libcd$getOwner();
			} else if (container instanceof class_1714) {
				return ((CraftingResultSlotAccessor) container.method_7611(0)).libcd$getPlayer();
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not access player due to mixin failures", e);
		}
	}

	public static StackInfo[] getInvStacks(class_1715 inv) {
		StackInfo[] stacks = new StackInfo[inv.method_5439()];
		for (int i = 0; i < inv.method_5439(); i++) {
			stacks[i] = new StackInfo(inv.method_5438(i));
		}
		return stacks;
	}
}
