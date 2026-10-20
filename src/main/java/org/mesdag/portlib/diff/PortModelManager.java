package org.mesdag.portlib.diff;

import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

@Diff
public class PortModelManager {
    public static final ResourceLocation GUI_ATLAS = ResourceLocation.withDefaultNamespace("textures/atlas/gui.png");
    public static final ResourceLocation GUI_ATLAS_INFO = ResourceLocation.withDefaultNamespace("gui");
    public static Map<ResourceLocation, AtlasSet.StitchResult> atlasPreparations;
}
