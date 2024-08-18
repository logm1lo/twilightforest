package twilightforest.client.renderer.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.beans.Autowired;
import twilightforest.beans.Configurable;
import twilightforest.entity.passive.Bird;
import twilightforest.util.IdPrefixUtil;

@Configurable
public class BirdRenderer<T extends Bird, M extends EntityModel<T>> extends MobRenderer<T, M> {

	@Autowired
	private IdPrefixUtil modidPrefixUtil;

	private final ResourceLocation texture;

	public BirdRenderer(EntityRendererProvider.Context context, M model, float shadowSize, String textureName) {
		super(context, model, shadowSize);
		this.texture = modidPrefixUtil.modelTexture(textureName);
	}

	@Override
	protected float getBob(T entity, float partialTicks) {
		float flapLength = entity.lastFlapLength + (entity.flapLength - entity.lastFlapLength) * partialTicks;
		float flapIntensity = entity.lastFlapIntensity + (entity.flapIntensity - entity.lastFlapIntensity) * partialTicks;
		return (Mth.sin(flapLength) + 1.0F) * flapIntensity;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return this.texture;
	}
}
