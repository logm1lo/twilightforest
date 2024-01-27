package twilightforest.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import twilightforest.TFRegistries;
import twilightforest.data.AtlasGenerator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Progression(
        ResourceLocation advancement,
        ItemStack icon,
        @Nullable ResourceLocation restriction,
        Component title,
        Component description,
        Component lockedDescription,
        Vec2i coordinates,
        List<ResourceKey<Progression>> parents,
        List<Vec2i> points) {

    public static final Codec<Progression> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
            ResourceLocation.CODEC.fieldOf("advancement").forGetter(Progression::advancement),
            ItemStack.ADVANCEMENT_ICON_CODEC.fieldOf("icon").forGetter(Progression::icon),
            ResourceLocation.CODEC.optionalFieldOf("restriction").forGetter(progression -> Optional.ofNullable(progression.restriction)),
            ComponentSerialization.CODEC.fieldOf("title").forGetter(Progression::title),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(Progression::description),
            ComponentSerialization.CODEC.fieldOf("locked_description").forGetter(Progression::lockedDescription),
            Codecs.VECTOR2I.fieldOf("coordinates").forGetter(Progression::coordinates),
            ResourceKey.codec(TFRegistries.Keys.PROGRESSIONS).listOf().fieldOf("parents").forGetter(Progression::parents),
            Codecs.VECTOR2I.listOf().fieldOf("points").forGetter(Progression::points)
    ).apply(recordCodecBuilder, Progression::make));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Progression make(
            ResourceLocation advancement,
            ItemStack icon,
            Optional<ResourceLocation> restriction,
            Component title,
            Component description,
            Component lockedDescription,
            Vec2i coordinates,
            List<ResourceKey<Progression>> parents,
            List<Vec2i> points) {
        return new Progression(advancement, icon, restriction.orElse(null), title, description, lockedDescription, coordinates, parents, points);
    }

    public static class Builder {
        @Nullable private ResourceLocation advancement = null;
        private ItemStack icon = ItemStack.EMPTY;
        @Nullable private ResourceLocation restriction = null;
        private Component title = Component.empty();
        private Component description = Component.empty();
        private Component lockedDescription = Component.empty();
        private Vec2i coordinates = Vec2i.ZERO;
        private final List<ResourceKey<Progression>> parents = new ArrayList<>();
        private final List<Vec2i> points = new ArrayList<>();

        public static Builder progression(ResourceLocation advancement, ItemLike icon, Component title, Component description, Component lockedDescription) {
            return progression(advancement, new ItemStack(icon.asItem()), title, description, lockedDescription);
        }

        public static Builder progression(ResourceLocation advancement, ItemStack icon, Component title, Component description, Component lockedDescription) {
            Builder builder = new Builder();
            builder.advancement = advancement;
            builder.icon = icon;
            builder.title = title;
            builder.description = description;
            builder.lockedDescription = lockedDescription;
            return builder;
        }

        public Builder restriction(ResourceLocation restriction) {
            this.restriction = restriction;
            return this;
        }

        public Builder coordinates(int x, int y) {
            return this.coordinates(new Vec2i(x, y));
        }

        public Builder coordinates(Vec2i coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder addParent(ResourceKey<Progression> resourceLocation) {
            this.parents.add(resourceLocation);
            return this;
        }

        public Builder addPoint(int x, int y) {
            return this.addPoint(new Vec2i(x, y));
        }

        public Builder addPoint(Vec2i point) {
            this.points.add(point);
            return this;
        }

        public Progression build() {
            if (this.restriction != null) AtlasGenerator.PROGRESSION_RESTRICTION_HELPER.add(this.restriction);
            return new Progression(this.advancement, this.icon, this.restriction, this.title, this.description, this.lockedDescription, this.coordinates, this.parents, this.points);
        }
    }
}
