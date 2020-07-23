package io.github.cottonmc.libcd.api.tweaker;

import blue.endless.jankson.JsonObject;
import java.util.concurrent.Executor;
import net.minecraft.class_3300;

public interface Tweaker {
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
	 * Prepare anything needed based on the script, like namespaces or other information. Called before each script is run.
	 * @param bridge The bridge provided for this script, including info like the namespace, script engine, and script text.
	 */
	default void prepareFor(ScriptBridge bridge) {}

	/**
	 * @return A JsonObject containing information useful for debugging. Called when `/libcd debug export` is run.
	 */
	JsonObject getDebugInfo();
}
