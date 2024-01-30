package twilightforest.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import twilightforest.TwilightForestMod;

public class TFDataMaps {

	public static final DataMapType<EntityType<?>, EntityType<?>> TRANSFORMATION_POWDER = DataMapType.builder(
			TwilightForestMod.prefix("transformation_powder"), Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE.byNameCodec()).synced(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), false).build();

	public static final DataMapType<Block, Block> CRUMBLE_HORN = DataMapType.builder(
			TwilightForestMod.prefix("crumble_horn"), Registries.BLOCK, BuiltInRegistries.BLOCK.byNameCodec()).synced(BuiltInRegistries.BLOCK.byNameCodec(), false).build();
}
