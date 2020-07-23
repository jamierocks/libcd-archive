package io.github.cottonmc.libcd.loot;

import io.github.cottonmc.libcd.api.tag.TagHelper;
import net.minecraft.class_117;
import net.minecraft.class_1792;
import net.minecraft.class_3494;
import net.minecraft.class_5341;
import net.minecraft.class_77;

public class DefaultedTagEntry extends class_77 {
	public DefaultedTagEntry(class_1792 item, int weight, int quality, class_5341[] conditions, class_117[] functions) {
		super(item, weight, quality, conditions, functions);
	}

	public static class_86<?> builder(class_3494<class_1792> itemTag) {
		return method_434((weight, quality, conditions, functions) ->
				new DefaultedTagEntry(TagHelper.ITEM.getDefaultEntry(itemTag), weight, quality, conditions, functions));
	}
}
