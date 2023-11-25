package twilightforest.data.custom;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;

public class TransformationPowderGenerator extends TransformationPowderProvider {

	public TransformationPowderGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, TwilightForestMod.ID, helper);
	}

	@Override
	public void registerTransforms() {
		addTwoWayTransform(TFEntities.MINOTAUR.value(), EntityType.ZOMBIFIED_PIGLIN);
		addTwoWayTransform(TFEntities.DEER.value(), EntityType.COW);
		addTwoWayTransform(TFEntities.BOAR.value(), EntityType.PIG);
		addTwoWayTransform(TFEntities.BIGHORN_SHEEP.value(), EntityType.SHEEP);
		addTwoWayTransform(TFEntities.DWARF_RABBIT.value(), EntityType.RABBIT);
		addTwoWayTransform(TFEntities.TINY_BIRD.value(), EntityType.PARROT);
		addTwoWayTransform(TFEntities.RAVEN.value(), EntityType.BAT);
		addTwoWayTransform(TFEntities.HOSTILE_WOLF.value(), EntityType.WOLF);
		addTwoWayTransform(TFEntities.PENGUIN.value(), EntityType.CHICKEN);
		addTwoWayTransform(TFEntities.HEDGE_SPIDER.value(), EntityType.SPIDER);
		addTwoWayTransform(TFEntities.SWARM_SPIDER.value(), EntityType.CAVE_SPIDER);
		addTwoWayTransform(TFEntities.WRAITH.value(), EntityType.VEX);
		addTwoWayTransform(TFEntities.SKELETON_DRUID.value(), EntityType.WITCH);
		addTwoWayTransform(TFEntities.CARMINITE_GHASTGUARD.value(), EntityType.GHAST);
		addTwoWayTransform(TFEntities.TOWERWOOD_BORER.value(), EntityType.SILVERFISH);
		addTwoWayTransform(TFEntities.MAZE_SLIME.value(), EntityType.SLIME);
	}
}
