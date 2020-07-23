package io.github.cottonmc.libcd.api.tweaker;

import javax.script.ScriptEngine;
import net.minecraft.class_2960;

/**
 * A bridge for specific LibCD hooks between Java and JSR-223 languages. An instance is provided to every script as `libcd`.
 * Contains information for other extension systems, such as the script engine, the text of the script, and the script's ID
 */
public class ScriptBridge {
	private ScriptEngine engine;
	private String scriptText;
	private class_2960 id;

	public ScriptBridge(ScriptEngine engine, String scriptText, class_2960 id) {
		this.engine = engine;
		this.scriptText = scriptText;
		this.id = id;
	}

	public Object require(String assistant) {
		return TweakerManager.INSTANCE.getAssistant(assistant, this);
	}

	//TODO: be able to require other scripts, instead of just assistants?

	public ScriptEngine getEngine() {
		return engine;
	}

	//TODO: is this helpful?
	public String getScriptText() {
		return scriptText;
	}

	public class_2960 getId() {
		return id;
	}
}
