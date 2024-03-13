package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

public record UpdateShieldPacket(int entityID, int temporaryShields, int permanentShields) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("update_shield_attachment");

	public UpdateShieldPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeInt(this.temporaryShields());
		buf.writeInt(this.permanentShields());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(UpdateShieldPacket message, PlayPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		//the level is not yet set in the payload context when a player logs in, so we need to fall back to the clientlevel instead
		if (ctx.flow().isClientbound()) {
			ctx.workHandler().execute(new Runnable() {
				@Override
				public void run() {
					Entity entity = ctx.level().orElse(Minecraft.getInstance().level).getEntity(message.entityID);
					if (entity instanceof LivingEntity living) {
						var attachment = living.getData(TFDataAttachments.FORTIFICATION_SHIELDS);
						attachment.setShields(living, message.temporaryShields, true);
						attachment.setShields(living, message.permanentShields, false);
					}
				}
			});
		}
	}
}
