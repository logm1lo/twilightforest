package twilightforest.network;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.client.DimensionSpecialEffectsManager;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.init.TFDimension;

public class StructureProtectionClearPacket implements CustomPacketPayload {

	public static final Type<StructureProtectionClearPacket> TYPE = new Type<>(TwilightForestMod.prefix("clear_protection_renderer"));
	public static final StreamCodec<RegistryFriendlyByteBuf, StructureProtectionClearPacket> STREAM_CODEC = StreamCodec.unit(new StructureProtectionClearPacket());

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(StructureProtectionClearPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			DimensionSpecialEffects info = DimensionSpecialEffectsManager.getForType(TFDimension.DIMENSION_RENDERER);

			// remove weather box if needed
			if (info instanceof TwilightForestRenderInfo) {
				TFWeatherRenderer.setProtectedBox(null);
			}
		});
	}
}
