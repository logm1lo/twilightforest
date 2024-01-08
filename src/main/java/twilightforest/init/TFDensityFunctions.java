package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.layer.vanillalegacy.BiomeTerrainWarpRouter;

public class TFDensityFunctions {
    public static final DeferredRegister<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, TwilightForestMod.ID);

    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<BiomeTerrainWarpRouter>> BIOME_DRIVEN = register("biome_driven", BiomeTerrainWarpRouter.CODEC);

    private static <T extends DensityFunction> DeferredHolder<Codec<? extends DensityFunction>, Codec<T>> register(String name, KeyDispatchDataCodec<T> keyCodec) {
        return DENSITY_FUNCTION_TYPES.register(name, keyCodec::codec);
    }
}
