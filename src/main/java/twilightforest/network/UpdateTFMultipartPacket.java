package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public record UpdateTFMultipartPacket(int entityId, @Nullable Entity entity, @Nullable Map<Integer, PartDataHolder> data) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("update_multipart_entity");

	public UpdateTFMultipartPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), null, new HashMap<>());
		int id;
		while ((id = buf.readInt()) > 0) {
			this.data.put(id, PartDataHolder.decode(buf));
		}
	}

	public UpdateTFMultipartPacket(Entity entity) {
		this(-1, entity, Arrays.stream(entity.getParts()).filter(part -> part instanceof TFPart<?>).map(part -> (TFPart<?>) part).collect(Collectors.toMap(TFPart::getId, TFPart::writeData)));
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		if (this.entity == null)
			throw new IllegalStateException("Null Entity while encoding UpdateTFMultipartPacket");
		if (this.data == null)
			throw new IllegalStateException("Null Data while encoding UpdateTFMultipartPacket");
		buf.writeInt(this.entity.getId());
		this.data.forEach((id, data) -> {
			buf.writeInt(id);
			data.encode(buf);
		});
		buf.writeInt(-1);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(UpdateTFMultipartPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			int eId = message.entity != null && message.entityId <= 0 ? message.entity.getId() : message.entityId; // Account for Singleplayer
			Entity ent = ctx.level().orElseThrow().getEntity(eId);
			if (ent != null && ent.isMultipartEntity()) {
				PartEntity<?>[] parts = ent.getParts();
				if (parts == null)
					return;
				for (PartEntity<?> part : parts) {
					if (part instanceof TFPart<?> tfPart) {
						if (message.data == null && message.entity != null && Arrays.stream(message.entity.getParts()).filter(Objects::nonNull).filter(p -> p.getId() == part.getId()).findFirst().orElseThrow() instanceof TFPart<?> otherPart)
							tfPart.readData(otherPart.writeData());  // Account for Singleplayer
						else if (message.data != null) {
							PartDataHolder data = message.data.get(tfPart.getId());
							if (data != null)
								tfPart.readData(data);
						}
					}
				}
			}
		});
	}

	public record PartDataHolder(double x, double y, double z,
								 float yRot, float xRot,
								 float width, float height,
								 boolean fixed,
								 @Nullable List<SynchedEntityData.DataValue<?>> data) {


		public void encode(FriendlyByteBuf buffer) {
			buffer.writeDouble(this.x());
			buffer.writeDouble(this.y());
			buffer.writeDouble(this.z());
			buffer.writeFloat(this.yRot());
			buffer.writeFloat(this.xRot());
			buffer.writeFloat(this.width());
			buffer.writeFloat(this.height());
			buffer.writeBoolean(this.fixed());
			if (this.data() != null) {
				for (SynchedEntityData.DataValue<?> datavalue : this.data()) {
					datavalue.write(buffer);
				}
			}
			buffer.writeByte(255);
		}

		static PartDataHolder decode(FriendlyByteBuf buffer) {
			return new PartDataHolder(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
					buffer.readFloat(), buffer.readFloat(),
					buffer.readFloat(), buffer.readFloat(),
					buffer.readBoolean(),
					unpack(buffer)
			);
		}

		private static List<SynchedEntityData.DataValue<?>> unpack(FriendlyByteBuf buf) {
			List<SynchedEntityData.DataValue<?>> list = new ArrayList<>();

			int i;
			while ((i = buf.readUnsignedByte()) != 255) {
				list.add(SynchedEntityData.DataValue.read(buf, i));
			}

			return list;
		}

	}
}
