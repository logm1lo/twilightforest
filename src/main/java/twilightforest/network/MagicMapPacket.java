package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.MagicMapItem;
import twilightforest.item.mapdata.TFMagicMapData;

// Rewraps vanilla ClientboundMapItemDataPacket to properly expose our custom decorations
public class MagicMapPacket implements CustomPacketPayload {

	public static final Type<MagicMapPacket> TYPE = new Type<>(TwilightForestMod.prefix("magic_map"));

	private final byte[] featureData;
	private final ClientboundMapItemDataPacket inner;

	public MagicMapPacket(TFMagicMapData mapData, ClientboundMapItemDataPacket inner) {
		this.featureData = mapData.serializeFeatures();
		this.inner = inner;
	}

	public MagicMapPacket(FriendlyByteBuf buf) {
		this.featureData = buf.readByteArray();
		this.inner = new ClientboundMapItemDataPacket(buf);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeByteArray(this.featureData);
		this.inner.write(buf);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(MagicMapPacket message, IPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		//the level is not yet set in the payload context when a player logs in, so we need to fall back to the clientlevel instead
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(new Runnable() {
				@Override
				public void run() {
					Level level = ctx.level().orElse(Minecraft.getInstance().level);
					// [VanillaCopy] ClientPlayNetHandler#handleMaps with our own mapdatas
					MapRenderer mapitemrenderer = Minecraft.getInstance().gameRenderer.getMapRenderer();
					String s = MagicMapItem.getMapName(message.inner.mapId().id());
					TFMagicMapData mapdata = TFMagicMapData.getMagicMapData(level, s);
					if (mapdata == null) {
						mapdata = new TFMagicMapData(0, 0, message.inner.scale(), false, false, message.inner.locked(), level.dimension());
						TFMagicMapData.registerMagicMapData(level, mapdata, s);
					}

					message.inner.applyToMap(mapdata);

					// TF - handle custom decorations
					mapdata.deserializeFeatures(message.featureData);

					mapitemrenderer.update(message.inner.mapId(), mapdata);
				}
			});
		}
	}
}