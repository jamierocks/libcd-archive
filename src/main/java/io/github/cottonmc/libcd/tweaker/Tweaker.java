package io.github.cottonmc.libcd.tweaker;

import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import io.github.cottonmc.libcd.legacy.LegacyTweaker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.minecraft.class_2960;
import net.minecraft.class_3300;

@Deprecated
/**
 * Use {@link io.github.cottonmc.libcd.api.tweaker.Tweaker} instead
 */
public interface Tweaker {

	/**
	 * Add a new tweaker to store data in.
	 * @param callName A unique name to call this tweaker by in scripts. Names shared with addAssistant(Factory).
	 * @param tweaker An instanceof Tweaker to call whenever reloading.
	 */
	static void addTweaker(String callName, Tweaker tweaker) {
		TweakerManager.INSTANCE.addTweaker(callName, new LegacyTweaker(tweaker));
	}

	/**
	 * Add a new assistant class for tweakers to access.
	 * DO NOT PASS TWEAKER INSTANCES HERE. They are automatically added in addTweaker.
	 * @param callName A unique name to call this object by in scripts. Names shared with addTweaker and addAssistantFactory.
	 * @param assistant An object of a class to use in scripts.
	 */
	static void addAssistant(String callName, Object assistant) {
		TweakerManager.INSTANCE.addLegacyAssistant(callName, assistant);
	}

	/**
	 * Add a factory for assistants which has methods affected by Script ID.
	 * @param callName A unique name to call this object by in scripts. Names shared with addTweaker and addAssistant.
	 * @param assistant A function that takes an identifier and returns an object of a class to use in scripts.
	 */
	static void addAssistantFactory(String callName, Function<class_2960, Object> assistant) {
		TweakerManager.INSTANCE.addLegacyAssistantFactory(callName, assistant);
	}

	/**
	 * Called whenever the /reload command is run, before scripts are run.
	 * Use this time to empty out any lists or maps you need to.
	 * @param manager The ResourceManager reloading tweakers.
	 */
	void prepareReload(class_3300 manager);

	/**
	 * Called whenever the /reload command is run, after scripts are run.
	 * Use this time to apply whatever you need to.
	 * @param manager The ResourceManager applying tweakers. Should be the same one called in prepareReload.
	 * @param executor The Executor applying tweakers.
	 */
	void applyReload(class_3300 manager, Executor executor);

	/**
	 * Called after all scripts have been run, to log what tweakers have been applied.
	 * @return The number of applied tweaks and the description of what type of tweak it is, ex. "12 recipes"
	 */
	String getApplyMessage();

	/**
	 * Prepare anything needed based on the script ID, like namespaces. Called before each script is run.
	 * @param scriptId The ID of the script about to be run.
	 */
	default void prepareFor(class_2960 scriptId) {}
}