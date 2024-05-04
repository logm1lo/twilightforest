package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;

public record ClientboundWhiskToTwilightForestPacket(boolean whiskingAway) implements CustomPacketPayload {
	public static final Type<ClientboundWhiskToTwilightForestPacket> TYPE = new Type<>(TwilightForestMod.prefix("whisk_away"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundWhiskToTwilightForestPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, ClientboundWhiskToTwilightForestPacket::whiskingAway,
		ClientboundWhiskToTwilightForestPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(ClientboundWhiskToTwilightForestPacket message, IPayloadContext ctx) {
		TFClientEvents.LoadingScreenEvents.useOurScreen(message.whiskingAway());
	}
}
