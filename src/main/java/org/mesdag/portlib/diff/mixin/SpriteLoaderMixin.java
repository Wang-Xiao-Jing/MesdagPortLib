package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.client.gui.PortGuiMetadataSection;
import org.mesdag.portlib.wrapper.common.extensions.IPortSpriteContentsExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(SpriteLoader.class)
public abstract class SpriteLoaderMixin {
    @Inject(method = "loadSprite", at = @At("RETURN"), cancellable = true)
    private static void portlib$retainMetadata(ResourceLocation location, Resource resource, CallbackInfoReturnable<SpriteContents> cir) {
        SpriteContents contents = cir.getReturnValue();
        if (contents == null) {
            return;
        }

        try {
            IPortSpriteContentsExtension.of(contents).portlib$setMetadata(PortGuiMetadataSection.cache(resource.metadata()));
        } catch (IOException exception) {
            PortLib.LOGGER.error("无法读取精灵元数据：{}", location, exception);
            contents.close();
            cir.setReturnValue(null);
        }
    }
}
