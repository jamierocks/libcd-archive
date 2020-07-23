package io.github.cottonmc.libcd;

import io.github.cottonmc.libcd.condition.ConditionalData;
import io.github.cottonmc.libcd.tweaker.RecipeTweaker;
import io.github.cottonmc.libcd.tweaker.Tweaker;
import io.github.cottonmc.libcd.tweaker.TweakerLoader;
import io.github.cottonmc.libcd.tweaker.TweakerStackGetter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_1847;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

public class LibCD implements ModInitializer {
	public static final String MODID = "libcd";

	public static final Logger logger = LogManager.getLogger();

	@Override
	public void onInitialize() {
		ConditionalData.init();
		ResourceManagerHelper.get(class_3264.field_14190).registerReloadListener(new TweakerLoader());
		Tweaker.addTweaker(RecipeTweaker.INSTANCE);
		TweakerStackGetter.registerGetter(new class_2960("minecraft", "potion"), (id) -> {
			class_1842 potion = class_1842.method_8048(id.toString());
			if (potion == class_1847.field_8984) return class_1799.field_8037;
			return class_1844.method_8061(new class_1799(class_1802.field_8574), potion);
		});
	}

	/**
	 * Moved to {@link ConditionalData#registerCondition(Identifier, Predicate)}
	 */
	@Deprecated
	public static void registerCondition(class_2960 id, Predicate<Object> condition) {
		ConditionalData.registerCondition(id, condition);
	}
}
