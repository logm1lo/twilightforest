package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.OreMeterItem;

public record SyncOreMeterPacket(ItemStack oreMeter, int slot) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("sync_ore_meter");

	public SyncOreMeterPacket(FriendlyByteBuf buf) {
		this(buf.readItem(), buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeItem(this.oreMeter());
		buf.writeInt(this.slot());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(SyncOreMeterPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			OreMeterItem.saveScanInfo(message.oreMeter(), OreMeterItem.getScanInfo(message.oreMeter()), OreMeterItem.getScannedChunk(message.oreMeter()).toLong(), OreMeterItem.getScannedBlocks(message.oreMeter()));
			ctx.player().orElseThrow().getSlot(message.slot()).set(message.oreMeter());
		});
	}
}
