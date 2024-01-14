package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public abstract class AbsoluteDifferenceFunction implements DensityFunction.SimpleFunction {
    public static Min min(int max, BlockPos pos) {
        return new Min(max, pos.getX(), pos.getZ());
    }

    public static Max max(int max, BlockPos pos) {
        return new Max(max, pos.getX(), pos.getZ());
    }

    protected final int max, centerX, centerZ;

    public AbsoluteDifferenceFunction(int max, int centerX, int centerZ) {
        this.max = max;
        this.centerX = centerX;
        this.centerZ = centerZ;
    }

    @Override
    public double minValue() {
        return 0;
    }

    @Override
    public double maxValue() {
        return this.max;
    }

    public static class Min extends AbsoluteDifferenceFunction {
        public static final KeyDispatchDataCodec<Min> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("max").forGetter(f -> f.max),
                Codec.INT.fieldOf("x_center").forGetter(f -> f.centerX),
                Codec.INT.fieldOf("z_center").forGetter(f -> f.centerZ)
        ).apply(instance, Min::new)));

        public Min(int max, int xCenter, int zCenter) {
            super(max, xCenter, zCenter);
        }

        @Override
        public double compute(FunctionContext context) {
            return Math.min(Math.min(Math.abs(context.blockX() - this.centerX), Math.abs(context.blockZ() - this.centerZ)), this.max);
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public static class Max extends AbsoluteDifferenceFunction {
        public static final KeyDispatchDataCodec<Max> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("max").forGetter(f -> f.max),
                Codec.INT.fieldOf("x_center").forGetter(f -> f.centerX),
                Codec.INT.fieldOf("z_center").forGetter(f -> f.centerZ)
        ).apply(instance, Max::new)));

        public Max(int max, int xCenter, int zCenter) {
            super(max, xCenter, zCenter);
        }

        @Override
        public double compute(FunctionContext context) {
            return Math.min(Math.max(Math.abs(context.blockX() - this.centerX), Math.abs(context.blockZ() - this.centerZ)), this.max);
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
}
