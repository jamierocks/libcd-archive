package io.github.cottonmc.libcd.mixin;

import com.google.common.base.Charsets;
import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.api.condition.ConditionalData;
import io.github.cottonmc.libcd.impl.ResourceSearcher;
import net.minecraft.class_2960;
import net.minecraft.class_3262;
import net.minecraft.class_3264;
import net.minecraft.class_3294;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.resource.*;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(class_3294.class)
public abstract class MixinNamespaceResourceManager implements class_3300, ResourceSearcher {
	@Shadow @Final protected List<class_3262> packList;

	@Shadow @Final private class_3264 type;

	@Shadow protected abstract boolean isPathAbsolute(class_2960 id);

	public boolean libcd$contains(class_2960 id) {
		if (!this.isPathAbsolute(id)) {
			return false;
		} else {
			for(int i = this.packList.size() - 1; i >= 0; --i) {
				class_3262 pack = this.packList.get(i);
				if (pack.method_14411(this.type, id)) {
					return true;
				}
			}

			return false;
		}
	}

	@Inject(method = "findResources", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void checkConditioalRecipes(String parent, Predicate<String> loadFilter, CallbackInfoReturnable<Collection<class_2960>> cir,
										List<class_2960> sortedResources) {
		List<class_2960> sortedCopy = new ArrayList<>(sortedResources);
		for (class_2960 id : sortedCopy) {
			//don't try to load for things that use mcmetas already!
			if (id.method_12832().contains(".mcmeta") || id.method_12832().contains(".png")) continue;
			class_2960 metaId = new class_2960(id.method_12836(), id.method_12832() + ".mcmeta");
			if (libcd$contains(metaId)) {
				try {
					class_3298 meta = method_14486(metaId);
					String metaText = IOUtils.toString(meta.method_14482(), Charsets.UTF_8);
					if (!ConditionalData.shouldLoad(id, metaText)) {
						sortedResources.remove(id);
					}
				} catch (IOException e) {
					CDCommons.logger.error("Error when accessing resource metadata for %s: %s", id.toString(), e.getMessage());
				}
			}
		}
	}

}
