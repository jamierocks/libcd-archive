package io.github.cottonmc.libcd.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.cottonmc.libcd.LibCD;
import io.github.cottonmc.libcd.api.tag.TagHelper;
import net.minecraft.class_117;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_3489;
import net.minecraft.class_3494;
import net.minecraft.class_3518;
import net.minecraft.class_4570;
import net.minecraft.class_85;

public class DefaultedTagEntrySerializer extends class_85.class_90<DefaultedTagEntry> {

	public DefaultedTagEntrySerializer() {
		super(new class_2960(LibCD.MODID, "defaulted_tag"), DefaultedTagEntry.class);
	}

	@Override
	protected DefaultedTagEntry fromJson(JsonObject entryJson, JsonDeserializationContext context, int weight, int quality, class_4570[] conditions, class_117[] functions) {
		String tagName = class_3518.method_15265(entryJson, "name");
		class_3494<class_1792> itemTag = class_3489.method_15106().method_15193(new class_2960(tagName));
		if (itemTag == null) {
			throw new JsonSyntaxException("Unknown tag " + tagName);
		}
		class_1792 item = TagHelper.ITEM.getDefaultEntry(itemTag);
		if (item == class_1802.field_8162) {
			throw new JsonSyntaxException("No items in tag " + tagName);
		}
		return new DefaultedTagEntry(item, weight, quality, conditions, functions);
	}
}
