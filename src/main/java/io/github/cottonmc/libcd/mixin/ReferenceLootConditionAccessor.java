package io.github.cottonmc.libcd.mixin;

import net.minecraft.class_2960;
import net.minecraft.class_4568;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_4568.class)
public interface ReferenceLootConditionAccessor {
    @Invoker("<init>")
    static class_4568 callConstructor(class_2960 id) {
        throw new AssertionError();
    }
}
