package io.github.cottonmc.libcd.tag;

import io.github.cottonmc.libcd.LibCD;
import io.github.cottonmc.libcd.api.tag.TagHelper;
import javax.annotation.Nullable;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3494;
import java.util.HashMap;
import java.util.Map;

public final class ItemTagHelper implements TagHelper<class_1792> {
	public static final ItemTagHelper INSTANCE = new ItemTagHelper();

	private final Map<class_3494<class_1792>, class_1792> defaultEntries = new HashMap<>();

	@Nullable
	@Override
	//TODO: What do we do with stuff like nether ores or mods that register multiple things to the same tag? Have extra tags for nether/end ores that get appended in?
	public class_1792 getDefaultEntry(class_3494<class_1792> tag) {
		if (defaultEntries.containsKey(tag)) {
			return defaultEntries.get(tag);
		}
		class_1792 ret = class_1802.field_8162;
		int currentPref = -1;
		for (class_1792 item : tag.method_15138()) {
			class_2960 id = class_2378.field_11142.method_10221(item);
			String namespace = id.method_12836();
			int index = LibCD.config.namespace_preference.indexOf(namespace);
			if (index == -1) {
				LibCD.config.namespace_preference.add(namespace);
				LibCD.saveConfig(LibCD.config);
				index = LibCD.config.namespace_preference.indexOf(namespace);
			}
			if (ret == class_1802.field_8162) {
				ret = item;
				currentPref = index;
			} else {
				if (currentPref > index) {
					ret = item;
					currentPref = index;
				}
			}
		}
		return ret;
	}

	public void reset() {
		defaultEntries.clear();
	}

	public void add(class_3494<class_1792> id, class_1792 value) {
		defaultEntries.put(id, value);
	}

	private ItemTagHelper() { }
}
