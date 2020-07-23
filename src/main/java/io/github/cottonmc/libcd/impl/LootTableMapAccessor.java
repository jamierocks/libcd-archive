package io.github.cottonmc.libcd.impl;

import java.util.Map;
import net.minecraft.class_2960;
import net.minecraft.class_4567;
import net.minecraft.class_52;

public interface LootTableMapAccessor {
	Map<class_2960, class_52> libcd$getLootTableMap();
	void libcd$setLootTableMap(Map<class_2960, class_52> map);
	class_4567 libcd$getConditionManager();
}
