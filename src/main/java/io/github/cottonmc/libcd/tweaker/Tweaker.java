package io.github.cottonmc.libcd.tweaker;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_3300;

public interface Tweaker {
	List<Tweaker> TWEAKERS = new ArrayList<>();

	/**
	 * Add a new tweaker to store data in.
	 * @param tweaker an instanceof Tweaker to call whenever reloading.
	 */
	static void addTweaker(Tweaker tweaker) {
		TWEAKERS.add(tweaker);
	}

	/**
	 * Called whenever the /reload command is run, before scripts are applied.
	 * Use this time to empty out any lists or maps you need to.
	 * @param manager The ResourceManager reloading tweakers.
	 */
	void prepareReload(class_3300 manager);

	/**
	 * Called after all scripts have been run, to log what tweakers have been applied.
	 * @return The number of applied tweaks and the description of what type of tweak it is, ex. "12 recipes"
	 */
	String getApplyMessage();
}