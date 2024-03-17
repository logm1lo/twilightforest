package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;

public record WipeOreMeterPacket(InteractionHand hand) implements CustomPacketPayload {
	public static final ResourceLocation ID = TwilightForestMod.prefix("wipe_ore_meter");

	public WipeOreMeterPacket(FriendlyByteBuf buf) {
		this(buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(this.hand() == InteractionHand.MAIN_HAND);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(WipeOreMeterPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			ItemStack heldStack = ctx.player().orElseThrow().getItemInHand(message.hand());
			if (heldStack.is(TFItems.ORE_METER)) {
				heldStack.getOrCreateTag().remove(OreMeterItem.NBT_SCAN_DATA);
				heldStack.removeData(TFDataAttachments.ORE_SCANNER);
			}
		});
	}
}
