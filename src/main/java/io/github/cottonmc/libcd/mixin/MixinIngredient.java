package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.IngredientAccessUtils;
import io.github.cottonmc.libcd.util.NbtMatchType;
import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_2487;
import net.minecraft.class_2520;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_1856.class)
public abstract class MixinIngredient implements IngredientAccessUtils {

	@Shadow private class_1799[] matchingStacks;

	@Shadow protected abstract void cacheMatchingStacks();

	private NbtMatchType type = NbtMatchType.NONE;
	@Inject(method = "method_8093", at = @At(value = "RETURN", ordinal = 2), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void checkStackNbt(class_1799 test, CallbackInfoReturnable<Boolean> cir, class_1799[] stackArray, int arrayLength, int i, class_1799 testAgainst) {
		if (!testAgainst.method_7985() || test.method_7969().isEmpty()) {
			if (type == NbtMatchType.EXACT && (test.method_7985() && !test.method_7969().isEmpty())) cir.setReturnValue(false);
			else cir.setReturnValue(true);
			return;
		}
		if (type != NbtMatchType.NONE && !test.method_7985()) {
			cir.setReturnValue(false);
			return;
		}
		class_2487 testTag = test.method_7948();
		class_2487 againstTag = testAgainst.method_7948();
		switch(type) {
			case FUZZY:
				for (String key : againstTag.method_10541()) {
					if (!testTag.method_10545(key)) cir.setReturnValue(false);
					class_2520 trial = testTag.method_10580(key);
					class_2520 against = againstTag.method_10580(key);
					if (trial.method_10711() == against.method_10711()) {
						if (!trial.method_10714().equals(against.method_10714())) {
							cir.setReturnValue(false);
							return;
						}
					}
				}
				cir.setReturnValue(true);
				break;
			case EXACT:
				cir.setReturnValue(testTag.method_10714().equals(againstTag.method_10714()));
				break;
			default:
				cir.setReturnValue(true);
		}
	}


	@Override
	public void libcd_setMatchType(NbtMatchType type) {
		this.type = type;
	}

	@Override
	public class_1799[] libcd_getStackArray() {
		cacheMatchingStacks();
		return matchingStacks;
	}
}
