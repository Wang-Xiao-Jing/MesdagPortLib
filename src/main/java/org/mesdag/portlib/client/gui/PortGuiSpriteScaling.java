package org.mesdag.portlib.client.gui;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.function.Function;

public interface PortGuiSpriteScaling {
    Codec<PortGuiSpriteScaling> CODEC = Type.CODEC.dispatch(PortGuiSpriteScaling::type, Type::codec);
    PortGuiSpriteScaling DEFAULT = new Stretch();

    Type type();

    record Stretch() implements PortGuiSpriteScaling {
        public static final Codec<Stretch> CODEC = Codec.unit(Stretch::new);

        @Override
        public Type type() {
            return Type.STRETCH;
        }
    }

    record Tile(int width, int height) implements PortGuiSpriteScaling {
        public static final Codec<Tile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(Tile::width),
                ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(Tile::height)
        ).apply(instance, Tile::new));

        @Override
        public Type type() {
            return Type.TILE;
        }
    }

    record NineSlice(int width, int height, Border border) implements PortGuiSpriteScaling {
        public static final Codec<NineSlice> CODEC = RecordCodecBuilder.<NineSlice>create(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(NineSlice::width),
                ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(NineSlice::height),
                Border.CODEC.fieldOf("border").forGetter(NineSlice::border)
        ).apply(instance, NineSlice::new)).flatXmap(NineSlice::validate, NineSlice::validate);

        private static DataResult<NineSlice> validate(NineSlice scaling) {
            Border border = scaling.border();
            if (border.left() >= scaling.width() - border.right() || border.top() >= scaling.height() - border.bottom()) {
                return DataResult.error(() -> "九宫格边框必须保留正尺寸的中心区域");
            }

            return DataResult.success(scaling);
        }

        @Override
        public Type type() {
            return Type.NINE_SLICE;
        }

        public record Border(int left, int top, int right, int bottom) {
            private static final Codec<Border> RECORD_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("left").forGetter(Border::left),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("top").forGetter(Border::top),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("right").forGetter(Border::right),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bottom").forGetter(Border::bottom)
            ).apply(instance, Border::new));
            public static final Codec<Border> CODEC = Codec.either(ExtraCodecs.POSITIVE_INT, RECORD_CODEC).xmap(
                    value -> value.map(Border::uniform, Function.identity()),
                    border -> border.isUniform() ? Either.left(border.left()) : Either.right(border));

            private static Border uniform(int value) {
                return new Border(value, value, value, value);
            }

            private boolean isUniform() {
                return left > 0 && left == top && top == right && right == bottom;
            }
        }
    }

    enum Type implements StringRepresentable {
        STRETCH("stretch", Stretch.CODEC),
        TILE("tile", Tile.CODEC),
        NINE_SLICE("nine_slice", NineSlice.CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private final String key;
        private final Codec<? extends PortGuiSpriteScaling> codec;

        private Type(String key, Codec<? extends PortGuiSpriteScaling> codec) {
            this.key = key;
            this.codec = codec;
        }

        @Override
        public String getSerializedName() {
            return key;
        }

        public Codec<? extends PortGuiSpriteScaling> codec() {
            return codec;
        }
    }
}
