package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.TFCavesCarver;

import java.util.Objects;

//this was all put into 1 class because it seems like a waste to have it in 2
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFCaveCarvers {
	public static final TFCavesCarver TFCAVES = new TFCavesCarver(CaveCarverConfiguration.CODEC, false);
	public static final TFCavesCarver HIGHLANDCAVES = new TFCavesCarver(CaveCarverConfiguration.CODEC, true);

	//this one has to stay, configured versions dont work otherwise
	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		if (Objects.equals(evt.getRegistryKey(), Registries.CARVER)) {
			evt.register(Registries.CARVER, helper -> helper.register(TwilightForestMod.prefix("tf_caves"), TFCAVES));
			evt.register(Registries.CARVER, helper -> helper.register(TwilightForestMod.prefix("highland_caves"), HIGHLANDCAVES));
		}
	}

	public static final ResourceKey<ConfiguredWorldCarver<?>> TFCAVES_CONFIGURED = registerKey("tf_caves");
	public static final ResourceKey<ConfiguredWorldCarver<?>> HIGHLANDCAVES_CONFIGURED = registerKey("highland_caves");

	private static ResourceKey<ConfiguredWorldCarver<?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_CARVER, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstapContext<ConfiguredWorldCarver<?>> context) {
		HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
		context.register(TFCAVES_CONFIGURED, TFCAVES.configured(new CaveCarverConfiguration(
				0.1F,
				UniformHeight.of(VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(-8)),
				ConstantFloat.of(0.6F),
				VerticalAnchor.bottom(),
				blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
				ConstantFloat.of(1F),
				ConstantFloat.of(0.9F),
				ConstantFloat.of(-0.7F)
		)));

		context.register(HIGHLANDCAVES_CONFIGURED, HIGHLANDCAVES.configured(new CaveCarverConfiguration(
				1f,
				BiasedToBottomHeight.of(VerticalAnchor.absolute(8), VerticalAnchor.absolute(32), 16),
				ConstantFloat.of(0.6f),
				VerticalAnchor.bottom(),
				blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
				UniformFloat.of(1.1f, 1.3f),
				ConstantFloat.of(1f),
				UniformFloat.of(-0.9F, -0.65F)
		)));
	}
}
