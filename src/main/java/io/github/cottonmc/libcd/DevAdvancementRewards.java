package io.github.cottonmc.libcd;

import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.api.advancement.AdvancementRewardsManager;
import io.github.cottonmc.libcd.api.init.AdvancementInitializer;
import net.minecraft.class_2960;

public class DevAdvancementRewards implements AdvancementInitializer {
	@Override
	public void initAdvancementRewards(AdvancementRewardsManager manager) {
		if (LibCD.isDevMode()) {
			manager.register(
					new class_2960("libcd:without_settings"),
					(serverPlayerEntity) -> CDCommons.logger.info(
							"%s earned libcd:without_settings",
							serverPlayerEntity.method_5476().method_10851())
			);
			manager.register(
					new class_2960("libcd:with_settings"),
					(serverPlayerEntity, settings) -> CDCommons.logger.info(
							"%s earned libcd:with_settings{setting1: %s}",
							serverPlayerEntity.method_5476().method_10851(),
							settings.get("setting1").getAsNumber())
			);
		}
	}
}
