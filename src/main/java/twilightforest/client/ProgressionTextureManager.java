package twilightforest.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;

@OnlyIn(Dist.CLIENT)
public class ProgressionTextureManager extends TextureAtlasHolder {
    public final static String PROGRESSION_RESTRICTION_PATH = "progression_restrictions";
    public static final ResourceLocation ATLAS_LOCATION = TwilightForestMod.prefix("textures/atlas/progression_restrictions.png");
    public static final ResourceLocation ATLAS_INFO_LOCATION = new ResourceLocation(PROGRESSION_RESTRICTION_PATH);

    public static ProgressionTextureManager instance;

    public ProgressionTextureManager(TextureManager textureManager) {
        super(textureManager, ATLAS_LOCATION, ATLAS_INFO_LOCATION);
    }

    public TextureAtlasSprite getLayerSprite(ResourceLocation restriction) {
        return this.getSprite(restriction.withPrefix(PROGRESSION_RESTRICTION_PATH + "/"));
    }
}