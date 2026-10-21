package org.mesdag.portlib.client.gui.components;

import net.minecraft.resources.ResourceLocation;

public record PortWidgetSprites(
        ResourceLocation enabled,
        ResourceLocation disabled,
        ResourceLocation enabledFocused,
        ResourceLocation disabledFocused
) {
    public PortWidgetSprites(ResourceLocation normal, ResourceLocation highlighted) {
        this(normal, normal, highlighted, highlighted);
    }

    public PortWidgetSprites(ResourceLocation enabled, ResourceLocation disabled, ResourceLocation highlighted) {
        this(enabled, disabled, highlighted, disabled);
    }

    public ResourceLocation get(boolean enabled, boolean focused) {
        if (enabled) {
            return focused ? enabledFocused : this.enabled;
        }
        return focused ? disabledFocused : disabled;
    }
}
