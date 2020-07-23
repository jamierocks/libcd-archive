package io.github.cottonmc.libcd.legacy;

import io.github.cottonmc.libcd.api.tweaker.TweakerStackFactory;
import io.github.cottonmc.libcd.tweaker.TweakerStackGetter;
import net.minecraft.class_1799;
import net.minecraft.class_2960;

public class LegacyGetter implements TweakerStackFactory {
	private TweakerStackGetter getter;

	public LegacyGetter(TweakerStackGetter getter) {
		this.getter = getter;
	}

	@Override
	public class_1799 getSpecialStack(class_2960 entry) {
		return getter.getSpecialStack(entry);
	}
}
