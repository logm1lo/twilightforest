package twilightforest.network;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.client.DimensionSpecialEffectsManager;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.init.TFDimension;

public record StructureProtectionPacket(BoundingBox box) implements CustomPacketPayload {

	public static final Type<StructureProtectionPacket> TYPE = new Type<>(TwilightForestMod.prefix("add_protection_renderer"));
	public static final StreamCodec<RegistryFriendlyByteBuf, StructureProtectionPacket> STREAM_CODEC = CustomPacketPayload.codec(StructureProtectionPacket::write, StructureProtectionPacket::new);

	public StructureProtectionPacket(FriendlyByteBuf buf) {
		this(new BoundingBox(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.box().minX());
		buf.writeInt(this.box().minY());
		buf.writeInt(this.box().minZ());
		buf.writeInt(this.box().maxX());
		buf.writeInt(this.box().maxY());
		buf.writeInt(this.box().maxZ());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(StructureProtectionPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			DimensionSpecialEffects info = DimensionSpecialEffectsManager.getForType(TFDimension.DIMENSION_RENDERER);

			// add weather box if needed
			if (info instanceof TwilightForestRenderInfo) {
				TFWeatherRenderer.setProtectedBox(message.box());
			}
		});
	}
}
