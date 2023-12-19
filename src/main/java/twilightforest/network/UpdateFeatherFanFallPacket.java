package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkEvent;
import twilightforest.init.TFDataAttachments;

public class UpdateFeatherFanFallPacket {
	private final int entityID;
	private final boolean falling;

	public UpdateFeatherFanFallPacket(int id, boolean falling) {
		this.entityID = id;
		this.falling = falling;
	}

	public UpdateFeatherFanFallPacket(FriendlyByteBuf buf) {
		this.entityID = buf.readInt();
		this.falling = buf.readBoolean();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID);
		buf.writeBoolean(this.falling);
	}

	public static class Handler {

		public static boolean onMessage(UpdateFeatherFanFallPacket message, NetworkEvent.Context ctx) {
			ctx.enqueueWork(() -> {
				Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
				if (entity instanceof Player) {
					entity.setData(TFDataAttachments.FEATHER_FAN, message.falling);
				}
			});

			ctx.setPacketHandled(true);
			return true;
		}
	}
}
