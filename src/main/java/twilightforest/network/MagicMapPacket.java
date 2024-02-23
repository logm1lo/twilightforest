package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.MagicMapItem;
import twilightforest.item.mapdata.TFMagicMapData;

// Rewraps vanilla ClientboundMapItemDataPacket to properly expose our custom decorations
public class MagicMapPacket implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("magic_map");

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

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeByteArray(this.featureData);
		this.inner.write(buf);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(MagicMapPacket message, PlayPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		//the level is not yet set in the payload context when a player logs in, so we need to fall back to the clientlevel instead
		if (ctx.flow().isClientbound()) {
			ctx.workHandler().execute(new Runnable() {
				@Override
				public void run() {
					Level level = ctx.level().orElse(Minecraft.getInstance().level);
					// [VanillaCopy] ClientPlayNetHandler#handleMaps with our own mapdatas
					MapRenderer mapitemrenderer = Minecraft.getInstance().gameRenderer.getMapRenderer();
					String s = MagicMapItem.getMapName(message.inner.getMapId());
					TFMagicMapData mapdata = TFMagicMapData.getMagicMapData(level, s);
					if (mapdata == null) {
						mapdata = new TFMagicMapData(0, 0, message.inner.getScale(), false, false, message.inner.isLocked(), level.dimension());
						TFMagicMapData.registerMagicMapData(level, mapdata, s);
					}

					message.inner.applyToMap(mapdata);

					// TF - handle custom decorations
					mapdata.deserializeFeatures(message.featureData);

					mapitemrenderer.update(message.inner.getMapId(), mapdata);
				}
			});
		}
	}
}