package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.PlayerScreenHandlerAccessor;
import net.minecraft.class_1657;
import net.minecraft.class_1723;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_1723.class)
public class MixinPlayerScreenHandler implements PlayerScreenHandlerAccessor {
	@Shadow @Final private class_1657 owner;

	@Override
	public class_1657 libcd$getOwner() {
		return owner;
	}
}
