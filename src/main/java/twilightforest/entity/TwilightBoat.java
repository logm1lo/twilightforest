package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.NetworkHooks;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;

public class TwilightBoat extends Boat {

	private static final EntityDataAccessor<Integer> BOAT_TYPE = SynchedEntityData.defineId(TwilightBoat.class, EntityDataSerializers.INT);

	public TwilightBoat(EntityType<? extends Boat> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	public TwilightBoat(Level level, double x, double y, double z) {
		this(TFEntities.BOAT.value(), level);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	public TwilightBoat.Type getTwilightBoatType() {
		return TwilightBoat.Type.byId(this.getEntityData().get(BOAT_TYPE));
	}

	@Override
	public Item getDropItem() {
		return switch (this.getTwilightBoatType()) {
			case TWILIGHT_OAK -> TFItems.TWILIGHT_OAK_BOAT.value();
			case CANOPY -> TFItems.CANOPY_BOAT.value();
			case MANGROVE -> TFItems.MANGROVE_BOAT.value();
			case DARKWOOD -> TFItems.DARK_BOAT.value();
			case TIME -> TFItems.TIME_BOAT.value();
			case TRANSFORMATION -> TFItems.TRANSFORMATION_BOAT.value();
			case MINING -> TFItems.MINING_BOAT.value();
			case SORTING -> TFItems.SORTING_BOAT.value();
		};
	}

	public void setTwilightBoatType(TwilightBoat.Type boatType) {
		this.getEntityData().set(BOAT_TYPE, boatType.ordinal());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(BOAT_TYPE, TwilightBoat.Type.TWILIGHT_OAK.ordinal());
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putString("Type", this.getTwilightBoatType().getName());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("Type", 8)) {
			this.setTwilightBoatType(TwilightBoat.Type.getTypeFromString(tag.getString("Type")));
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public enum Type {
		TWILIGHT_OAK(TFBlocks.TWILIGHT_OAK_PLANKS.value(), "twilight_oak"),
		CANOPY(TFBlocks.CANOPY_PLANKS.value(), "canopy"),
		MANGROVE(TFBlocks.MANGROVE_PLANKS.value(), "mangrove"),
		DARKWOOD(TFBlocks.DARK_PLANKS.value(), "dark"),
		TIME(TFBlocks.TIME_PLANKS.value(), "time"),
		TRANSFORMATION(TFBlocks.TRANSFORMATION_PLANKS.value(), "transformation"),
		MINING(TFBlocks.MINING_PLANKS.value(), "mining"),
		SORTING(TFBlocks.SORTING_PLANKS.value(), "sorting");

		private final String name;
		private final Block block;

		Type(Block block, String name) {
			this.name = name;
			this.block = block;
		}

		public String getName() {
			return this.name;
		}

		public Block asPlank() {
			return this.block;
		}

		public String toString() {
			return this.name;
		}

		public static TwilightBoat.Type byId(int id) {
			TwilightBoat.Type[] types = values();
			if (id < 0 || id >= types.length) {
				id = 0;
			}

			return types[id];
		}

		public static TwilightBoat.Type getTypeFromString(String nameIn) {
			TwilightBoat.Type[] types = values();

			for (Type type : types) {
				if (type.getName().equals(nameIn)) {
					return type;
				}
			}

			return types[0];
		}
	}
}