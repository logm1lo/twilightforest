package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.OreMeterItem;

import java.util.HashMap;

public record WipeOreMeterPacket(ItemStack oreMeter, InteractionHand hand) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("wipe_ore_meter");

	public WipeOreMeterPacket(FriendlyByteBuf buf) {
		this(buf.readItem(), buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeItem(this.oreMeter());
		buf.writeBoolean(this.hand() == InteractionHand.MAIN_HAND);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(WipeOreMeterPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			OreMeterItem.saveScanInfo(message.oreMeter(), new HashMap<>(), 0L, 0);
			OreMeterItem.clearAssignedBlock(message.oreMeter());
			ctx.player().orElseThrow().setItemInHand(message.hand(), message.oreMeter());
		});
	}
}
