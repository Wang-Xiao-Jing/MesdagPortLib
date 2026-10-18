package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.mesdag.portlib.diff.Diff;

public interface IPortSpriteContentsExtension {
    ResourceMetadata metadata();

    @Diff
    void portlib$setMetadata(ResourceMetadata metadata);

    static IPortSpriteContentsExtension of(SpriteContents contents) {
        return (IPortSpriteContentsExtension) contents;
    }
}
