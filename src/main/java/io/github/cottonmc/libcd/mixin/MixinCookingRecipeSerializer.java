package io.github.cottonmc.libcd.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_1869;
import net.minecraft.class_1874;
import net.minecraft.class_2960;
import net.minecraft.class_3518;
import net.minecraft.class_3957;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_3957.class)
public class MixinCookingRecipeSerializer<T extends class_1874> {
	@Shadow @Final private int cookingTime;

	@Shadow @Final private class_3957.class_3958<T> recipeFactory;

	@Inject(method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/AbstractCookingRecipe;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;getString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;", ordinal = 0),
			cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void read(class_2960 id, JsonObject json, CallbackInfoReturnable<T> info,
					  String group, JsonElement ingElem, class_1856 ingredient) {
		JsonElement elem = json.get("result");
		if (elem instanceof JsonObject) {
			class_1799 stack = class_1869.method_8155((JsonObject)elem);
			float experience = class_3518.method_15277(json, "experience", 0.0F);
			int cookingtime = class_3518.method_15282(json, "cookingtime", this.cookingTime);
			info.setReturnValue(this.recipeFactory.create(id, group, ingredient, stack, experience, cookingtime));
		}
	}

}
