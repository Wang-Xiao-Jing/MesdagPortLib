package org.mesdag.portlib.client.gui.components;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record PortSprite(ResourceLocation path, int textureW, int textureH) {
    private static final String GUI_SPRITE_DIRECTORY = "textures/gui/sprites/";
    private static final String PNG_SUFFIX = ".png";

    public PortSprite {
        if (!path.getPath().endsWith(PNG_SUFFIX)) {
            path = ResourceLocation.fromNamespaceAndPath(path.getNamespace(), GUI_SPRITE_DIRECTORY + path.getPath() + PNG_SUFFIX);
        }
    }

    /// GUI 目录外的显式 PNG 保留直接纹理绘制，不进入 GUI 图集。
    public @Nullable ResourceLocation spriteId() {
        String value = path.getPath();
        if (!value.startsWith(GUI_SPRITE_DIRECTORY) || !value.endsWith(PNG_SUFFIX)) {
            return null;
        }

        return ResourceLocation.fromNamespaceAndPath(path.getNamespace(), value.substring(GUI_SPRITE_DIRECTORY.length(), value.length() - PNG_SUFFIX.length()));
    }
}
