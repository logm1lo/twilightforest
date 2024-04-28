package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;

public class TFBannerPatterns {
	public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, TwilightForestMod.ID);

	public static final DeferredHolder<BannerPattern, BannerPattern> NAGA = register("naga", "tfn");
	public static final DeferredHolder<BannerPattern, BannerPattern> LICH = register("lich", "tfl");
	public static final DeferredHolder<BannerPattern, BannerPattern> MINOSHROOM = register("minoshroom", "tfm");
	public static final DeferredHolder<BannerPattern, BannerPattern> HYDRA = register("hydra", "tfh");
	public static final DeferredHolder<BannerPattern, BannerPattern> KNIGHT_PHANTOM = register("knight_phantom", "tfp");
	public static final DeferredHolder<BannerPattern, BannerPattern> UR_GHAST = register("ur_ghast", "tfg");
	public static final DeferredHolder<BannerPattern, BannerPattern> ALPHA_YETI = register("alpha_yeti", "tfy");
	public static final DeferredHolder<BannerPattern, BannerPattern> SNOW_QUEEN = register("snow_queen", "tfq");
	public static final DeferredHolder<BannerPattern, BannerPattern> QUEST_RAM = register("quest_ram", "tfr");

	private static DeferredHolder<BannerPattern, BannerPattern> register(String name, String translationKey) {
		return BANNER_PATTERNS.register(name, () -> new BannerPattern(TwilightForestMod.prefix(name), translationKey));
	}
}
