package io.github.cottonmc.libcd.tweaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import net.minecraft.class_3300;

public interface Tweaker {
	List<Tweaker> TWEAKERS = new ArrayList<>();
	Map<String, Object> ASSISTANTS = new HashMap<>();

	/**
	 * Deprecated; use {@link Tweaker#addTweaker(String, Tweaker)} instead, as it will add the object as an assistant too.
	 * @param tweaker The tweaker to add.
	 */
	@Deprecated
	static void addTweaker(Tweaker tweaker) {
		TWEAKERS.add(tweaker);
	}

	/**
	 * Add a new tweaker to store data in.
	 * @param callName A unique name to call this tweaker by in scripts. Names shared with addAssistant.
	 * @param tweaker An instanceof Tweaker to call whenever reloading.
	 */
	static void addTweaker(String callName, Tweaker tweaker) {
		TWEAKERS.add(tweaker);
		ASSISTANTS.put(callName, tweaker);
	}

	/**
	 * Add a new assistant class for tweakers to access.
	 * DO NOT PASS TWEAKER INSTANCES HERE. They are automatically added in addTweaker.
	 * @param callName A unique name to call this object by in scripts. Names shared with addTweaker.
	 * @param assistant An object of a class to use in scripts.
	 */
	static void addAssistant(String callName, Object assistant) {
		ASSISTANTS.put(callName, assistant);
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
}