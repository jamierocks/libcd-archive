package io.github.cottonmc.libcd.tweaker;

import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import io.github.cottonmc.libcd.api.tweaker.TweakerStackFactory;
import io.github.cottonmc.libcd.legacy.LegacyGetter;
import net.minecraft.class_1799;
import net.minecraft.class_2960;

@Deprecated
/**
 * use {@link TweakerStackFactory} instead
 */
public interface TweakerStackGetter {

	static void registerGetter(class_2960 id, TweakerStackGetter getter) {
		TweakerManager.INSTANCE.addStackFactory(id, new LegacyGetter(getter));
	}

	/**
	 * Get an ItemStack from a registered processor
	 * @param entry The Identifier of the entry to get
	 * @return the proper ItemStack for the given Identifier, or an empty stack if the entry doesn't exist
	 */
	class_1799 getSpecialStack(class_2960 entry);
}
