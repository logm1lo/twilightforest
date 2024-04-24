package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;

public record WipeOreMeterPacket(InteractionHand hand) implements CustomPacketPayload {

	public static final Type<WipeOreMeterPacket> TYPE = new Type<>(TwilightForestMod.prefix("wipe_ore_meter"));
	public static final StreamCodec<RegistryFriendlyByteBuf, WipeOreMeterPacket> STREAM_CODEC = CustomPacketPayload.codec(WipeOreMeterPacket::write, WipeOreMeterPacket::new);

	public WipeOreMeterPacket(FriendlyByteBuf buf) {
		this(buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(this.hand() == InteractionHand.MAIN_HAND);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	//TODO data component
	public static void handle(WipeOreMeterPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			ItemStack heldStack = ctx.player().getItemInHand(message.hand());
			if (heldStack.is(TFItems.ORE_METER)) {
				heldStack.getOrCreateTag().remove(OreMeterItem.NBT_SCAN_DATA);
				heldStack.removeData(TFDataAttachments.ORE_SCANNER);
			}
		});
	}
}
