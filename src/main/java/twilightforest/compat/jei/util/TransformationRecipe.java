package twilightforest.compat.jei.util;

import net.minecraft.world.entity.EntityType;

public record TransformationRecipe(EntityType<?> input, EntityType<?> output, boolean isReversible) {
}
