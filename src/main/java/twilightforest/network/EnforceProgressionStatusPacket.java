package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.util.IdPrefixUtil;

public record EnforceProgressionStatusPacket(boolean enforce) implements CustomPacketPayload {

	@Autowired
	private static IdPrefixUtil modidPrefixUtil;

	public static final Lazy<Type<EnforceProgressionStatusPacket>> TYPE = Lazy.of(() -> new Type<>(modidPrefixUtil.prefix("sync_progression_status")));
	public static final StreamCodec<RegistryFriendlyByteBuf, EnforceProgressionStatusPacket> STREAM_CODEC = CustomPacketPayload.codec(EnforceProgressionStatusPacket::write, EnforceProgressionStatusPacket::new);

	public EnforceProgressionStatusPacket(FriendlyByteBuf buf) {
		this(buf.readBoolean());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(this.enforce);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE.get();
	}

	public static void handle(EnforceProgressionStatusPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() ->
			Minecraft.getInstance().level.getGameRules().getRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(message.enforce(), null)
		);
	}
}
