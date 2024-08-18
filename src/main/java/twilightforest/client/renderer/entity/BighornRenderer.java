package twilightforest.client.renderer.entity;

import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.neoforged.neoforge.common.util.Lazy;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.beans.Configurable;
import twilightforest.util.IdPrefixUtil;

@Configurable
public class BighornRenderer extends SheepRenderer {

	@Autowired
	private IdPrefixUtil modidPrefixUtil;

	private final Lazy<ResourceLocation> TEXTURE = Lazy.of(() -> modidPrefixUtil.modelTexture("bighorn.png"));

	@SuppressWarnings("unchecked")
	public BighornRenderer(EntityRendererProvider.Context context, SheepModel<? extends Sheep> baseModel, float shadowSize) {
		super(context);
		this.shadowRadius = shadowSize;
		this.model = (SheepModel<Sheep>) baseModel;
		this.addLayer(new SheepFurLayer(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(Sheep entity) {
		return TEXTURE.get();
	}
}
