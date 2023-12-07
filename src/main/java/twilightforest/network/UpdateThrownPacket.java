package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkEvent;
import twilightforest.init.TFDataAttachments;
import twilightforest.capabilities.YetiThrowAttachment;

public class UpdateThrownPacket {

	private final int entityID;
	private final boolean thrown;
	private int thrower = 0;
	private final int throwCooldown;

	public UpdateThrownPacket(int id, YetiThrowAttachment attachment) {
		this.entityID = id;
		this.thrown = attachment.getThrown();
		this.throwCooldown = attachment.getThrowCooldown();
		if (attachment.getThrower() != null) {
			this.thrower = attachment.getThrower().getId();
		}
	}

	public UpdateThrownPacket(FriendlyByteBuf buf) {
		this.entityID = buf.readInt();
		this.thrown = buf.readBoolean();
		this.thrower = buf.readInt();
		this.throwCooldown = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID);
		buf.writeBoolean(this.thrown);
		buf.writeInt(this.thrower);
		buf.writeInt(this.throwCooldown);
	}

	public static class Handler {

		public static boolean onMessage(UpdateThrownPacket message, NetworkEvent.Context ctx) {
			ctx.enqueueWork(() -> {
				Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
				if (entity instanceof Player player) {
					var attachment = player.getData(TFDataAttachments.YETI_THROWING);
					LivingEntity thrower = message.thrower != 0 ? (LivingEntity) Minecraft.getInstance().level.getEntity(message.thrower) : null;
					attachment.setThrown(player, message.thrown, thrower);
					attachment.setThrowCooldown(player, message.throwCooldown);
				}
			});

			ctx.setPacketHandled(true);
			return true;
		}
	}
}
