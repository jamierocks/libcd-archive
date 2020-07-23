package io.github.cottonmc.libcd.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import io.github.cottonmc.libcd.api.tag.TagHelper;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1869;
import net.minecraft.class_2487;
import net.minecraft.class_2509;
import net.minecraft.class_2960;
import net.minecraft.class_3489;
import net.minecraft.class_3518;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1869.class)
public class MixinShapedRecipe {
	//TODO: nbt crafting support?
	@Inject(method = "getItemStack", at = @At("HEAD"), cancellable = true)
	private static void loadResource(JsonObject json, CallbackInfoReturnable<class_1799> info) {
		if (json.has("tag")) {
			String tagName = class_3518.method_15265(json, "tag");
			class_2960 id = new class_2960(tagName);
			net.minecraft.class_3494<class_1792> itemTag = class_3489.method_15106().method_15193(id);
			if (itemTag == null) {
				throw new JsonSyntaxException("Unknown tag " + tagName);
			}
			class_1792 item = TagHelper.ITEM.getDefaultEntry(itemTag);
			if (item == class_1802.field_8162) {
				throw new JsonSyntaxException("No items in tag " + tagName);
			}
			int count = class_3518.method_15282(json, "count", 1);
			class_1799 stack = new class_1799(item, count);
			if (json.has("data")) {
				JsonObject data = class_3518.method_15296(json, "data");
				net.minecraft.class_2520 tag = Dynamic.convert(JsonOps.INSTANCE, class_2509.field_11560, data);
				if (tag instanceof class_2487) {
					stack.method_7980((class_2487)tag);
				}
			}
			info.setReturnValue(stack);
		}
	}
}
