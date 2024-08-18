package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Lazy;
import twilightforest.beans.Autowired;
import twilightforest.beans.Configurable;
import twilightforest.client.model.entity.WraithModel;
import twilightforest.entity.monster.Wraith;
import twilightforest.util.IdPrefixUtil;

@Configurable
public class WraithRenderer extends HumanoidMobRenderer<Wraith, WraithModel> {

	@Autowired
	private IdPrefixUtil modidPrefixUtil;

	private final Lazy<ResourceLocation> TEXTURE = Lazy.of(() -> modidPrefixUtil.modelTexture("ghost.png"));

	public WraithRenderer(EntityRendererProvider.Context context, WraithModel model, float shadowSize) {
		super(context, model, shadowSize);
	}

	@Override
	public ResourceLocation getTextureLocation(Wraith wraith) {
		return TEXTURE.get();
	}
}
