package io.github.cottonmc.libcd.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.cottonmc.libcd.impl.CuttingRecipeFactoryInvoker;
import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_1869;
import net.minecraft.class_2960;
import net.minecraft.class_3972;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_3972.class_3973.class)
public class MixinCuttingRecipeSerializer<T extends class_3972> {

	@Shadow @Final private class_3972.class_3973.class_3974<T> recipeFactory;

	@Inject(method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/CuttingRecipe;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;getString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;", ordinal = 0),
			cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void read(class_2960 id, JsonObject json, CallbackInfoReturnable<T> info,
					  String group, class_1856 ingredient) {
		JsonElement elem = json.get("result");
		if (elem instanceof JsonObject) {
			class_1799 stack = class_1869.method_8155((JsonObject)elem);
			info.setReturnValue(this.recipeFactory.create(id, group, ingredient, stack));
		}
	}
}
