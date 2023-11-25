package twilightforest.client;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import twilightforest.TwilightForestMod;
import twilightforest.block.AuroraBrickBlock;
import twilightforest.block.CastleDoorBlock;
import twilightforest.block.HollowLogClimbable;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.item.ArcticArmorItem;
import twilightforest.util.ColorUtil;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ColorHandler {

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {

		BlockColors blockColors = event.getBlockColors();

		event.register((state, getter, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : ColorUtil.hsvToRGB(getter == null ? 0.45F : AuroraBrickBlock.rippleFractialNoise(2, 128.0f, pos != null ? pos.above(128) : new BlockPos(0, 0, 0), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f), TFBlocks.AURORA_BLOCK.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			int normalColor = blockColors.getColor(TFBlocks.AURORA_BLOCK.value().defaultBlockState(), getter, pos, tintIndex);

			int red = (normalColor >> 16) & 255;
			int blue = normalColor & 255;
			int green = (normalColor >> 8) & 255;

			float[] hsb = ColorUtil.rgbToHSV(red, green, blue);

			return ColorUtil.hsvToRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
		}, TFBlocks.AURORA_PILLAR.value(), TFBlocks.AURORA_SLAB.value(), TFBlocks.AURORALIZED_GLASS.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (getter == null || pos == null) {
				return FoliageColor.getDefaultColor();
			}

			int red = 0;
			int grn = 0;
			int blu = 0;

			for (int dz = -1; dz <= 1; ++dz) {
				for (int dx = -1; dx <= 1; ++dx) {
					int i2 = BiomeColors.getAverageFoliageColor(getter, pos.offset(dx, 0, dz));
					red += (i2 & 16711680) >> 16;
					grn += (i2 & 65280) >> 8;
					blu += i2 & 255;
				}
			}

			return (red / 9 & 255) << 16 | (grn / 9 & 255) << 8 | blu / 9 & 255;
		}, TFBlocks.DARK_LEAVES.value(), TFBlocks.HARDENED_DARK_LEAVES.value(), TFBlocks.GIANT_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : blockColors.getColor(Blocks.GRASS.defaultBlockState(), getter, pos, tintIndex), TFBlocks.SMOKER.value(), TFBlocks.FIRE_JET.value());
		event.register((state, getter, pos, tintIndex) -> getter != null && pos != null ? 2129968 : 7455580, TFBlocks.HUGE_LILY_PAD.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (getter == null || pos == null) {
				return 106 << 16 | 156 << 8 | 23;
			} else {
				int red, green, blue;

				int fade = pos.getX() * 16 + pos.getY() * 16 + pos.getZ() * 16;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 106 + fall * 251);
				green = (int) (spring * 156 + fall * 108);
				blue = (int) (spring * 23 + fall * 27);

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.TIME_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (getter == null || pos == null) {
				return 108 << 16 | 204 << 8 | 234;
			} else {
				int red, green, blue;

				int fade = pos.getX() * 27 + pos.getY() * 63 + pos.getZ() * 39;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 108 + fall * 96);
				green = (int) (spring * 204 + fall * 107);
				blue = (int) (spring * 234 + fall * 121);

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.TRANSFORMATION_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (getter == null || pos == null) {
				return 252 << 16 | 241 << 8 | 68;
			} else {
				int red, green, blue;

				int fade = pos.getX() * 31 + pos.getY() * 33 + pos.getZ() * 32;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 252 + fall * 237);
				green = (int) (spring * 241 + fall * 172);
				blue = (int) (spring * 68 + fall * 9);

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.MINING_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (getter == null || pos == null) {
				return 54 << 16 | 76 << 8 | 3;
			} else {
				int red, green, blue;

				int fade = pos.getX() * 63 + pos.getY() * 63 + pos.getZ() * 63;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 54 + fall * 168);
				green = (int) (spring * 76 + fall * 199);
				blue = (int) (spring * 3 + fall * 43);

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.SORTING_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (getter == null || pos == null) {
				return -1;
			} else {
				float f = AuroraBrickBlock.rippleFractialNoise(2, 32.0f, pos, 0.4f, 1.0f, 2f);
				return ColorUtil.hsvToRGB(0.1f, 1f - f, (f + 2f) / 3f);
			}
		}, TFBlocks.TOWERWOOD.value(), TFBlocks.CRACKED_TOWERWOOD.value(), TFBlocks.INFESTED_TOWERWOOD.value(), TFBlocks.MOSSY_TOWERWOOD.value());
		event.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x48B518;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				return (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
			}
		}, TFBlocks.TWILIGHT_OAK_LEAVES.value());
		event.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x609860;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				int normalColor = (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
				// canopy colorizer
				return ((normalColor & 0xFEFEFE) + 0x469A66) / 2;
			}
		}, TFBlocks.CANOPY_LEAVES.value());
		event.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x80A755;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				int normalColor = (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
				// mangrove colors
				return ((normalColor & 0xFEFEFE) + 0xC0E694) / 2;
			}
		}, TFBlocks.MANGROVE_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (getter == null || pos == null) {
				return 0x48B518;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						int color = BiomeColors.getAverageFoliageColor(getter, pos);
						red += (color & 0xFF0000) >> 16;
						green += (color & 0xFF00) >> 8;
						blue += color & 0xFF;
					}
				}

				// RAINBOW!
				red = pos.getX() * 32 + pos.getY() * 16;
				if ((red & 256) != 0) {
					red = 255 - (red & 255);
				}
				red &= 255;

				green = pos.getY() * 32 + pos.getZ() * 16;
				if ((green & 256) != 0) {
					green = 255 - (green & 255);
				}
				green ^= 255;

				blue = pos.getX() * 16 + pos.getZ() * 32;
				if ((blue & 256) != 0) {
					blue = 255 - (blue & 255);
				}
				blue &= 255;

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.RAINBOW_OAK_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> FoliageColor.getEvergreenColor(), TFBlocks.BEANSTALK_LEAVES.value(), TFBlocks.THORN_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex != 0) {
				return 0xFFFFFF;
			} else {
				if (getter != null && pos != null) {
					return BiomeColors.getAverageFoliageColor(getter, pos);
				} else {
					return FoliageColor.getDefaultColor();
				}
			}
		}, TFBlocks.FALLEN_LEAVES.value());
		event.register((state, getter, pos, tintIndex) -> {
			if ((tintIndex & 1) == 1) {
				return 0xFFFFFF;
			} else {
				if (getter != null && pos != null) {
					return BiomeColors.getAverageGrassColor(getter, pos);
				} else {
					return GrassColor.get(0.5D, 1.0D);
				}
			}
		}, TFBlocks.FIDDLEHEAD.value(), TFBlocks.POTTED_FIDDLEHEAD.value());
		event.register((state, getter, pos, tintIndex) -> {
					if ((tintIndex & 1) == 0) {
						return 0xFFFFFF;
					} else {
						return GrassColor.get(0.5D, 1.0D);
					}
				}, TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.value(), TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.value(),
				TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.value(), TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.value());
		event.register((state, getter, pos, tintIndex) -> {
					if (state.getValue(HollowLogClimbable.VARIANT) != HollowLogVariants.Climbable.VINE || (tintIndex & 1) == 0) {
						return 0xFFFFFF;
					} else {
						if (getter != null && pos != null) {
							return BiomeColors.getAverageFoliageColor(getter, pos);
						} else {
							return FoliageColor.getDefaultColor();
						}
					}
				}, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.value(), TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.value(), TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.value(), TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.value());
		event.register((state, getter, pos, tintIndex) -> GrassColor.get(0.5D, 1.0D),
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.value(), /*TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.value(), TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.value(), TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.value(), TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.value(),*/ TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.value(), TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.value() //TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.value(),
				/*TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.value(), TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.value(), TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.value(), TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.value(), TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.value(), TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.value(), TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.value()*/);
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			int color = 0xFF00FF;
			if (state.getBlock() instanceof CastleDoorBlock && state.getValue(CastleDoorBlock.ACTIVE) && !state.getValue(CastleDoorBlock.VANISHED)) return color ^ 0xFFFFFF;
			return color;
		}, TFBlocks.PINK_CASTLE_RUNE_BRICK.value(), TFBlocks.PINK_CASTLE_DOOR.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			int color = 0x00FFFF;
			if (state.getBlock() instanceof CastleDoorBlock && state.getValue(CastleDoorBlock.ACTIVE) && !state.getValue(CastleDoorBlock.VANISHED)) return color ^ 0xFFFFFF;
			return color;
		}, TFBlocks.BLUE_CASTLE_RUNE_BRICK.value(), TFBlocks.BLUE_CASTLE_DOOR.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			int color = 0xFFFF00;
			if (state.getBlock() instanceof CastleDoorBlock && state.getValue(CastleDoorBlock.ACTIVE) && !state.getValue(CastleDoorBlock.VANISHED)) return color ^ 0xFFFFFF;
			return color;
		}, TFBlocks.YELLOW_CASTLE_RUNE_BRICK.value(), TFBlocks.YELLOW_CASTLE_DOOR.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			int color = 0x4B0082;
			if (state.getBlock() instanceof CastleDoorBlock && state.getValue(CastleDoorBlock.ACTIVE) && !state.getValue(CastleDoorBlock.VANISHED)) return color ^ 0xFFFFFF;
			return color;
		}, TFBlocks.VIOLET_CASTLE_RUNE_BRICK.value(), TFBlocks.VIOLET_CASTLE_DOOR.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x5C1074;
		}, TFBlocks.VIOLET_FORCE_FIELD.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFA057E;
		}, TFBlocks.PINK_FORCE_FIELD.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFF5B02;
		}, TFBlocks.ORANGE_FORCE_FIELD.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x89E701;
		}, TFBlocks.GREEN_FORCE_FIELD.value());
		event.register((state, getter, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x0DDEFF;
		}, TFBlocks.BLUE_FORCE_FIELD.value());
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		BlockColors blockColors = event.getBlockColors();

		event.register((stack, tintIndex) -> {
					if (stack.getItem() instanceof BlockItem blocc)
						return blockColors.getColor(blocc.getBlock().defaultBlockState(), null, null, tintIndex);
					return 0xFFFFFF;
				},
				TFBlocks.AURORA_BLOCK.value(), TFBlocks.AURORA_PILLAR.value(), TFBlocks.AURORA_SLAB.value(), TFBlocks.AURORALIZED_GLASS.value(), TFBlocks.DARK_LEAVES.value(), TFBlocks.GIANT_LEAVES.value(), TFBlocks.SMOKER.value(), TFBlocks.FIRE_JET.value(),
				TFBlocks.TIME_LEAVES.value(), TFBlocks.TRANSFORMATION_LEAVES.value(), TFBlocks.MINING_LEAVES.value(), TFBlocks.SORTING_LEAVES.value(), TFBlocks.TWILIGHT_OAK_LEAVES.value(), TFBlocks.CANOPY_LEAVES.value(), TFBlocks.MANGROVE_LEAVES.value(), TFBlocks.RAINBOW_OAK_LEAVES.value(), TFBlocks.THORN_LEAVES.value(), TFBlocks.BEANSTALK_LEAVES.value(),
				TFBlocks.FALLEN_LEAVES.value(), TFBlocks.FIDDLEHEAD.value(), TFBlocks.POTTED_FIDDLEHEAD.value(), TFBlocks.PINK_CASTLE_RUNE_BRICK.value(), TFBlocks.BLUE_CASTLE_RUNE_BRICK.value(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.value(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.value(),
				TFBlocks.YELLOW_CASTLE_DOOR.value(), TFBlocks.BLUE_CASTLE_DOOR.value(), TFBlocks.PINK_CASTLE_DOOR.value(), TFBlocks.VIOLET_CASTLE_DOOR.value(), TFBlocks.PINK_FORCE_FIELD.value(), TFBlocks.BLUE_FORCE_FIELD.value(), TFBlocks.GREEN_FORCE_FIELD.value(), TFBlocks.ORANGE_FORCE_FIELD.value(), TFBlocks.VIOLET_FORCE_FIELD.value(), TFBlocks.HUGE_LILY_PAD.value(),
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.value(), /*TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.value(), TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.value(), TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.value(), TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.value(),*/ TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.value(), TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.value()//, TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.value(),
				/*TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.value(), TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.value(), TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.value(), TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.value(), TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.value(), TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.value(), TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.value()*/);

		event.register((stack, tintIndex) ->
						stack.getItem() instanceof ArcticArmorItem arctic
								? (arctic).getColor(stack, tintIndex)
								: 0xFFFFFF,
				TFItems.ARCTIC_HELMET.value(), TFItems.ARCTIC_CHESTPLATE.value(), TFItems.ARCTIC_LEGGINGS.value(), TFItems.ARCTIC_BOOTS.value());

		event.register((stack, tintIndex) ->
						tintIndex > 0 ? -1 : PotionUtils.getColor(stack),
				TFItems.BRITTLE_FLASK.value(), TFItems.GREATER_FLASK.value());
	}
}
