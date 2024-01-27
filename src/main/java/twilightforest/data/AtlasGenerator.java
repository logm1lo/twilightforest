package twilightforest.data;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import twilightforest.TwilightForestMod;
import twilightforest.client.MagicPaintingTextureManager;
import twilightforest.client.ProgressionTextureManager;
import twilightforest.entity.MagicPaintingVariant;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class AtlasGenerator extends SpriteSourceProvider {
	public static final List<ResourceLocation> PROGRESSION_RESTRICTION_HELPER = new ArrayList<>();
	public static final Map<ResourceLocation, MagicPaintingVariant> MAGIC_PAINTING_HELPER = new HashMap<>();
	public static final List<ResourceLocation> LOADED_FRAMES = new ArrayList<>();

	public AtlasGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
		super(output, provider, TwilightForestMod.ID, helper);
	}

	@Override
	protected void gather() {
		this.atlas(SHIELD_PATTERNS_ATLAS).addSource(new SingleFile(TwilightForestMod.prefix("entity/knightmetal_shield"), Optional.empty()));

		this.atlas(MagicPaintingTextureManager.ATLAS_INFO_LOCATION).addSource(new SingleFile(MagicPaintingTextureManager.BACK_SPRITE_LOCATION, Optional.empty()));
		this.loadFrame(MagicPaintingTextureManager.FRAME_SPRITE_LOCATION);

		PROGRESSION_RESTRICTION_HELPER.forEach((location) -> {
			location = location.withPrefix(ProgressionTextureManager.PROGRESSION_RESTRICTION_PATH + "/");
			this.atlas(ProgressionTextureManager.ATLAS_INFO_LOCATION).addSource(new SingleFile(location, Optional.empty()));
		});

		MAGIC_PAINTING_HELPER.forEach((location, parallaxVariant) -> {
			location = location.withPrefix(MagicPaintingTextureManager.MAGIC_PAINTING_PATH + "/");
			for (MagicPaintingVariant.Layer layer : parallaxVariant.layers()) {
				this.atlas(MagicPaintingTextureManager.ATLAS_INFO_LOCATION).addSource(new SingleFile(location.withSuffix("/" + layer.path()), Optional.empty()));
			}
			this.loadFrame(ResourceLocation.tryParse(parallaxVariant.framePath()));
		});
	}

	protected void loadFrame(@Nullable ResourceLocation resourceLocation) {
		if (resourceLocation != null) {
			if (!LOADED_FRAMES.contains(resourceLocation)) {
				LOADED_FRAMES.add(resourceLocation);
				this.atlas(MagicPaintingTextureManager.ATLAS_INFO_LOCATION).addSource(new SingleFile(resourceLocation, Optional.empty()));
			}
		}
	}
}
