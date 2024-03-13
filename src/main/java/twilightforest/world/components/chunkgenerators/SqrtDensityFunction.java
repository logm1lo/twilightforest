package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record SqrtDensityFunction(DensityFunction input) implements DensityFunction.SimpleFunction {
	public static final KeyDispatchDataCodec<SqrtDensityFunction> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.create(instance -> instance.group(
			DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(SqrtDensityFunction::input)
	).apply(instance, SqrtDensityFunction::new)));

	@Override
	public double compute(FunctionContext context) {
		double sqrt = Math.sqrt(this.input.compute(context));
		return sqrt;
	}

	@Override
	public double minValue() {
		return this.input.minValue();
	}

	@Override
	public double maxValue() {
		return this.input.maxValue();
	}

	@Override
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	@Override
	public DensityFunction mapAll(Visitor visitor) {
		return visitor.apply(new SqrtDensityFunction(this.input.mapAll(visitor)));
	}
}
