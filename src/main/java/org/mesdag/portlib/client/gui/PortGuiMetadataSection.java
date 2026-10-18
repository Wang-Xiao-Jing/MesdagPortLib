package org.mesdag.portlib.client.gui;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.mesdag.portlib.PortLib;

import java.util.Optional;
import java.util.function.Supplier;

public record PortGuiMetadataSection(PortGuiSpriteScaling scaling) {
    public static final PortGuiMetadataSection DEFAULT = new PortGuiMetadataSection(PortGuiSpriteScaling.DEFAULT);
    public static final Codec<PortGuiMetadataSection> CODEC = PortGuiSpriteScaling.CODEC
            .optionalFieldOf("scaling", PortGuiSpriteScaling.DEFAULT).xmap(PortGuiMetadataSection::new, PortGuiMetadataSection::scaling).codec();
    public static final MetadataSectionSerializer<PortGuiMetadataSection> TYPE = new MetadataSectionSerializer<>() {
        @Override
        public String getMetadataSectionName() {
            return "gui";
        }

        @Override
        public PortGuiMetadataSection fromJson(JsonObject json) {
            return CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(PortLib.LOGGER::error)
                    .orElseThrow(() -> new JsonParseException("无效的 GUI 精灵元数据"));
        }
    };

    /// 只缓存新增的 GUI 段；动画和其他元数据继续交给 1.20.1 原生解析。
    public static ResourceMetadata cache(ResourceMetadata metadata) {
        Supplier<Optional<PortGuiMetadataSection>> gui = Suppliers.memoize(() -> metadata.getSection(TYPE));
        return new ResourceMetadata() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> Optional<T> getSection(MetadataSectionSerializer<T> serializer) {
                return serializer == TYPE ? (Optional<T>) (Optional<?>) gui.get() : metadata.getSection(serializer);
            }
        };
    }
}
