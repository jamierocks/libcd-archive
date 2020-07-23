package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.LootTableMapAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import net.minecraft.class_2960;
import net.minecraft.class_4567;
import net.minecraft.class_52;
import net.minecraft.class_60;

@Mixin(class_60.class)
public class MixinLootManager implements LootTableMapAccessor {
	@Shadow private Map<class_2960, class_52> suppliers;

	@Shadow @Final private class_4567 conditionManager;

	@Override
	public Map<class_2960, class_52> libcd$getLootTableMap() {
		return suppliers;
	}

	@Override
	public void libcd$setLootTableMap(Map<class_2960, class_52> map) {
		this.suppliers = map;
	}

	@Override
	public class_4567 libcd$getConditionManager() {
		return conditionManager;
	}
}
