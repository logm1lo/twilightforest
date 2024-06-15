package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.effect.MobEffects;
import twilightforest.entity.monster.LichMinion;
import twilightforest.util.ColorModifier;

public class LichMinionModel extends ZombieModel<LichMinion> {

	private boolean hasStrength;

	public LichMinionModel(ModelPart root) {
		super(root);
	}

	@Override
	public void prepareMobModel(LichMinion entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.hasStrength = entity.getEffect(MobEffects.DAMAGE_BOOST) != null;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		if (hasStrength) {
			super.renderToBuffer(stack, builder, light, overlay, ColorModifier.begin(color).red(ColorModifier.QUARTER).blue(ColorModifier.QUARTER).build());
		} else {
			super.renderToBuffer(stack, builder, light, overlay, ColorModifier.begin(color).red(ColorModifier.HALF).blue(ColorModifier.HALF).build());
		}
	}
}
