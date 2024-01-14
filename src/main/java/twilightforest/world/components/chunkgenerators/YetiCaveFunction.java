package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record YetiCaveFunction(int centerX, int bottomY, int centerZ) implements DensityFunction.SimpleFunction {
    public static final KeyDispatchDataCodec<YetiCaveFunction> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x_center").forGetter(YetiCaveFunction::centerX),
            Codec.INT.fieldOf("y_bottom").forGetter(YetiCaveFunction::bottomY),
            Codec.INT.fieldOf("z_center").forGetter(YetiCaveFunction::centerZ)
    ).apply(instance, YetiCaveFunction::new)));

    public static YetiCaveFunction fromPos(BlockPos blockPos) {
        return new YetiCaveFunction(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public double compute(FunctionContext context) {
        final float dX = context.blockX() - this.centerX;
        final float dY = context.blockY() - this.bottomY;
        final float dZ = context.blockZ() - this.centerZ;
        final float top = 24;
        //if (dY > top) return 0;

        float max = Math.max(Math.abs(dX), Math.abs(dZ));
        float min = Math.min(Math.abs(dX), Math.abs(dZ));

        float entrances = top - 16 - min - dY * 0.25f;

        if (true) return entrances;

        float outside = top - max - dY * 0.25f;

        float ret = Math.min(outside, -entrances);

        return Mth.clamp(ret, -1, 1);
    }

    @Override
    public double minValue() {
        return -1;
    }

    @Override
    public double maxValue() {
        return 1;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
