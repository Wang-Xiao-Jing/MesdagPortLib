package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.mesdag.portlib.wrapper.common.extensions.IPortSpriteContentsExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SpriteContents.class)
public abstract class SpriteContentsMixin implements IPortSpriteContentsExtension {
    @Unique
    private ResourceMetadata portlib$metadata = ResourceMetadata.EMPTY;

    @Override
    public ResourceMetadata metadata() {
        return portlib$metadata;
    }

    @Override
    public void portlib$setMetadata(ResourceMetadata metadata) {
        this.portlib$metadata = metadata;
    }
}
