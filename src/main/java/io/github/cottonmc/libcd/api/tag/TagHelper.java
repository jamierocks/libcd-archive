package io.github.cottonmc.libcd.api.tag;

import io.github.cottonmc.libcd.tag.ItemTagHelper;
import net.minecraft.class_1792;
import net.minecraft.class_3494;

public interface TagHelper<T> {
	TagHelper<class_1792> ITEM = ItemTagHelper.INSTANCE; //TODO: does this make it impossible to have a separate API module? I have no idea how to fix it if so...

	T getDefaultEntry(class_3494<T> tag);
}
