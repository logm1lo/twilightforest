package twilightforest.util;

import net.minecraft.util.FastColor;

import java.util.function.Function;

public class ColorModifier {

	public static final Function<Integer, Integer> QUARTER = o -> (int) (o * 0.25F);
	public static final Function<Integer, Integer> HALF = o -> (int) (o * 0.5F);

	private final int color;
	private Function<Integer, Integer> modifiedRed = o -> o;
	private Function<Integer, Integer> modifiedGreen = o -> o;
	private Function<Integer, Integer> modifiedBlue = o -> o;
	private Function<Integer, Integer> modifiedAlpha = o -> o;

	private ColorModifier(int color) {
		this.color = color;
	}

	public static ColorModifier begin(int color) {
		return new ColorModifier(color);
	}

	public ColorModifier red(Function<Integer, Integer> red) {
		this.modifiedRed = red;
		return this;
	}

	public ColorModifier green(Function<Integer, Integer> green) {
		this.modifiedGreen = green;
		return this;
	}

	public ColorModifier blue(Function<Integer, Integer> blue) {
		this.modifiedBlue = blue;
		return this;
	}

	public ColorModifier alpha(Function<Integer, Integer> alpha) {
		this.modifiedAlpha = alpha;
		return this;
	}

	public int build() {
		int red = FastColor.ARGB32.red(this.color);
		int green = FastColor.ARGB32.green(this.color);
		int blue = FastColor.ARGB32.blue(this.color);
		int alpha = FastColor.ARGB32.alpha(this.color);
		return this.modifiedAlpha.apply(alpha) << 24 | this.modifiedRed.apply(red) << 16 | this.modifiedGreen.apply(green) << 8 | this.modifiedBlue.apply(blue);
	}

//	public ColorModifier modifyColors(float redMultiplier, float greenMultiplier, float blueMultiplier) {
//		int red = FastColor.ARGB32.red(this.color);
//		int green = FastColor.ARGB32.green(this.color);
//		int blue = FastColor.ARGB32.blue(this.color);
//		return new ColorModifier((this.color & 0XFF000000) | (int) (red * redMultiplier) << 16 | (int) (green * greenMultiplier) << 8 | (int) (blue * blueMultiplier));
//	}
//
//	public ColorModifier modifyAlpha(float alphaMultiplier) {
//		int alpha = FastColor.ARGB32.alpha(this.color);
//		return new ColorModifier((this.color & 0X00FFFFFF) | (int) (alpha * alphaMultiplier) << 24);
//	}
}
