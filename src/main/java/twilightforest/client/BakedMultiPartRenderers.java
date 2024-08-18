package twilightforest.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Lazy;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HydraHeadModel;
import twilightforest.client.model.entity.HydraNeckModel;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.client.renderer.entity.*;
import twilightforest.entity.TFPart;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraNeck;
import twilightforest.entity.boss.NagaSegment;
import twilightforest.entity.boss.SnowQueenIceShield;

import java.util.HashMap;
import java.util.Map;

public class BakedMultiPartRenderers {
	private static final Map<ResourceLocation, Lazy<EntityRenderer<?>>> renderers = new HashMap<>();

	public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
		renderers.put(TFPart.RENDERER.get(), Lazy.of(() -> new NoopRenderer<>(context)));
		renderers.put(HydraHead.RENDERER.get(), Lazy.of(() -> new HydraHeadRenderer<>(context, new HydraHeadModel<>(context.bakeLayer(TFModelLayers.HYDRA_HEAD)))));
		renderers.put(HydraNeck.RENDERER, Lazy.of(() -> new HydraNeckRenderer<>(context, new HydraNeckModel(context.bakeLayer(TFModelLayers.HYDRA_NECK)))));
		renderers.put(SnowQueenIceShield.RENDERER, Lazy.of(() -> new SnowQueenIceShieldRenderer<>(context)));
		renderers.put(NagaSegment.RENDERER, Lazy.of(() -> new NagaSegmentRenderer<>(context, new NagaModel<>(context.bakeLayer(TFModelLayers.NAGA_BODY)))));
	}

	public static EntityRenderer<?> lookup(ResourceLocation location) {
		return renderers.get(location).get();
	}
}
