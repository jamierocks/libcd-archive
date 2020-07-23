package io.github.cottonmc.libcd.api.condition;

import net.minecraft.class_2960;


public class ConditionManager {
	public static final ConditionManager INSTANCE = new ConditionManager();

	private ConditionManager() {}

	/**
	 * Register a condition that recipes can use as a requirement for loading.
	 * @param id The Id of the condition, namespaced to prevent duplicates.
	 * @param condition What must be true for the condition to be met.
	 */
	public void registerCondition(class_2960 id, Condition condition) {
		ConditionalData.conditions.put(id, condition);
	}

}
