package twilightforest.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.QuestRam;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;

import java.text.DecimalFormat;
import java.util.*;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFOverlays {
	//for some reason we need a 256x256 texture to actually render anything so i'll just make this a generic icons sheet
	//if we want to add any more overlay things in the future, we can simply add more icons!
	private static final ResourceLocation TF_ICONS_SHEET = TwilightForestMod.prefix("textures/gui/tf_icons.png");
	public static Map<Long, OreMeterInfoCache> ORE_METER_STAT_CACHE = new HashMap<>();

	@SubscribeEvent
	public static void registerOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), TwilightForestMod.prefix("quest_ram_indicator"), (gui, graphics, partialTick, screenWidth, screenHeight) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			if (player != null && !minecraft.options.hideGui && TFConfig.CLIENT_CONFIG.showQuestRamCrosshairIndicator.get()) {
				RenderSystem.enableBlend();
				renderIndicator(minecraft, graphics, gui, player, screenWidth, screenHeight);
				RenderSystem.disableBlend();
			}
		});
		event.registerAboveAll(TwilightForestMod.prefix("ore_meter_stats"), (gui, graphics, partialTick, screenWidth, screenHeight) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			if (player != null && !minecraft.options.hideGui && !gui.getDebugOverlay().showDebugScreen() && minecraft.screen == null) {
				renderOreMeterStats(graphics, player);
			}
		});
	}

	public static void renderIndicator(Minecraft minecraft, GuiGraphics graphics, Gui gui, Player player, int screenWidth, int screenHeight) {
		Options options = minecraft.options;
		if (options.getCameraType().isFirstPerson()) {
			if (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || gui.canRenderCrosshairForSpectator(minecraft.hitResult)) {
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				int j = ((screenHeight - 1) / 2) - 11;
				int k = ((screenWidth - 1) / 2) - 3;
				if (minecraft.crosshairPickEntity instanceof QuestRam ram) {
					ItemStack stack = player.getInventory().getItem(player.getInventory().selected);
					if (!stack.isEmpty() && stack.is(ItemTags.WOOL)) {
						if (ram.guessColor(stack) != null && !ram.isColorPresent(Objects.requireNonNull(ram.guessColor(stack)))) {
							graphics.blit(TF_ICONS_SHEET, k, j, 0, 0, 7, 7);
						} else {
							graphics.blit(TF_ICONS_SHEET, k, j, 7, 0, 7, 7);
						}
					}
				}
			}
		}
	}

	public static void renderOreMeterStats(GuiGraphics graphics, Player player) {
		if (player.isHolding(TFItems.ORE_METER.get())) {
			InteractionHand handToUse = player.getItemInHand(InteractionHand.MAIN_HAND).is(TFItems.ORE_METER.get()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			ItemStack selectedMeter = player.getItemInHand(handToUse);
			RenderSystem.disableDepthTest();
			RenderSystem.enableDepthTest();
			if (OreMeterItem.isLoading(selectedMeter)) {
				int dots = (OreMeterItem.getLoadProgress(selectedMeter) / 5) % 3;
				Component component = Component.translatable("misc.twilightforest.ore_meter_loading");
				for (int i = 0; i <= dots; i++) {
					component = component.copy().append(".");
				}
				graphics.fill(0, 0, 56, 16, 0x9b000000);
				graphics.drawString(Minecraft.getInstance().font, component, 4, 4, 16777215, false);
			} else if (!OreMeterItem.getScanInfo(selectedMeter).isEmpty()) {
				long identifier = OreMeterItem.getID(selectedMeter);
				if (identifier != 0L && !ORE_METER_STAT_CACHE.containsKey(identifier)) {
					initTooltips(identifier, selectedMeter);
				}
				if (ORE_METER_STAT_CACHE.containsKey(identifier)) {
					int yOff = 4;
					graphics.fill(0, 0, ORE_METER_STAT_CACHE.get(identifier).longestStringLength() + 8, ORE_METER_STAT_CACHE.get(identifier).components().size() * 10 + 6, 0x9b000000);
					for (Component component : ORE_METER_STAT_CACHE.get(identifier).components()) {
						graphics.drawString(Minecraft.getInstance().font, component, 4, yOff, 16777215, false);
						yOff += 10;
					}
				}
			}
		}
	}


	private static final DecimalFormat FORMAT = new DecimalFormat("#.###");

	public static void initTooltips(long id, ItemStack meter) {
		List<Component> tooltips = new ArrayList<>();
		ChunkPos pos = OreMeterItem.getScannedChunk(meter);
		int totalScanned = OreMeterItem.getScannedBlocks(meter);
		tooltips.add(Component.translatable("misc.twilightforest.ore_meter_range", OreMeterItem.getRange(meter), pos.x, pos.z));
		Component totalBlocks = Component.translatable("misc.twilightforest.ore_meter_total", totalScanned);
		tooltips.add(totalBlocks);

		int longestTotal = Minecraft.getInstance().font.width(totalBlocks);
		for (Map.Entry<String, Integer> entry : OreMeterItem.getScanInfo(meter).entrySet()) {
			float percentage =  ((float)entry.getValue() / totalScanned) * 100.0F;
			Component formattedEntry = Component.translatable("misc.twilightforest.ore_meter_info", Component.translatable(entry.getKey()), entry.getValue(), FORMAT.format(percentage));
			tooltips.add(formattedEntry);
			longestTotal = Math.max(longestTotal, Minecraft.getInstance().font.width(formattedEntry.getString()));
		}
		ORE_METER_STAT_CACHE.put(id, new OreMeterInfoCache(tooltips, longestTotal));
	}

	public record OreMeterInfoCache(List<Component> components, int longestStringLength) {}
}
