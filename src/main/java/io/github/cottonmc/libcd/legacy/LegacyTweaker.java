package io.github.cottonmc.libcd.legacy;

import blue.endless.jankson.JsonObject;
import io.github.cottonmc.libcd.api.tweaker.ScriptBridge;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
import java.util.concurrent.Executor;
import net.minecraft.class_3300;

public class LegacyTweaker implements Tweaker {
	private io.github.cottonmc.libcd.tweaker.Tweaker tweaker;

	public LegacyTweaker(io.github.cottonmc.libcd.tweaker.Tweaker tweaker) {
		this.tweaker = tweaker;
	}

	@Override
	public void prepareReload(class_3300 manager) {
		tweaker.prepareReload(manager);
	}

	@Override
	public void applyReload(class_3300 manager, Executor executor) {
		tweaker.applyReload(manager, executor);
	}

	@Override
	public String getApplyMessage() {
		return tweaker.getApplyMessage();
	}

	@Override
	public void prepareFor(ScriptBridge bridge) {
		tweaker.prepareFor(bridge.getId());
	}

	@Override
	public JsonObject getDebugInfo() {
		return new JsonObject();
	}
}
