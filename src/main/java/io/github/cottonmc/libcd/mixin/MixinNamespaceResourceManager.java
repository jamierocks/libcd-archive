package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.impl.ResourceSearcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import net.minecraft.class_2960;
import net.minecraft.class_3262;
import net.minecraft.class_3264;
import net.minecraft.class_3294;
import net.minecraft.class_3300;

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

//	@Inject(method = "findResources", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
//	private void checkConditioalRecipes(String parent, Predicate<String> loadFilter, CallbackInfoReturnable<Collection<Identifier>> cir,
//										List<Identifier> sortedResources) {
//		List<Identifier> sortedCopy = new ArrayList<>(sortedResources);
//		for (Identifier id : sortedCopy) {
//			//don't try to load for things that use mcmetas already!
//			if (id.getPath().contains(".mcmeta") || id.getPath().contains(".png")) continue;
//			Identifier metaId = new Identifier(id.getNamespace(), id.getPath() + ".mcmeta");
//			if (libcd$contains(metaId)) {
//				try {
//					Resource meta = getResource(metaId);
//					String metaText = IOUtils.toString(meta.getInputStream(), Charsets.UTF_8);
//					if (!ConditionalData.shouldLoad(id, metaText)) {
//						sortedResources.remove(id);
//					}
//				} catch (IOException e) {
//					CDCommons.logger.error("Error when accessing resource metadata for %s: %s", id.toString(), e.getMessage());
//				}
//			}
//		}
//	}

}
