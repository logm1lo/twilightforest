package twilightforest.world.components;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.function.Function;

//Framework taken from CaveWorldCarver, everything worth knowing is documented for easier changes in the future
public class TFCavesCarver extends WorldCarver<CaveCarverConfiguration> {
	private final boolean isHighlands;

	public TFCavesCarver(Codec<CaveCarverConfiguration> codec, boolean isHighlands) {
		super(codec);
		this.liquids = ImmutableSet.of(Fluids.WATER, Fluids.LAVA);
		this.isHighlands = isHighlands;
	}

	@Override
	public boolean isStartChunk(CaveCarverConfiguration config, RandomSource rand) {
		// Highland caves instead spawn when the troll caves structure is nearby, and with special location ruls
		return this.isHighlands || rand.nextFloat() <= config.probability;
	}

	@Override
	public boolean carve(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomePos, RandomSource random, Aquifer aquifer, ChunkPos accessPos, CarvingMask mask) {
        if (this.isHighlands && (Mth.clamp(LegacyLandmarkPlacements.manhattanDistanceFromLandmarkCenter(accessPos.x, accessPos.z), 0, 0b11) & 0b1) == 1)
			return false; // If highlands, enforces a binary grid of possible placements around the structure center, with center being one of the zero tiles

		int i = SectionPos.sectionToBlockCoord(this.getRange() * 2 - 1);

		// If highlands, only roll chance to generate even 1 cave. Otherwise, limited caves spawn for regular TF underground
        int caveCount = this.isHighlands ? random.nextInt(2) : random.nextInt(this.getCaveBound());

		for (int caveIndex = 0; caveIndex < caveCount; ++caveIndex) {
			double x = accessPos.getBlockX(random.nextInt(16));
			double y = config.y.sample(random, ctx);
			double z = accessPos.getBlockZ(random.nextInt(16));
			double horiz = config.horizontalRadiusMultiplier.sample(random);
			double vert = config.verticalRadiusMultiplier.sample(random);
			double floor = config.floorLevel.sample(random);
			CarveSkipChecker checker = (context, dX, dY, dZ, yPos) -> shouldSkip(dX, dY, dZ, floor);
			int tunnelCount = 1;
			if (this.isHighlands || random.nextInt(4) == 0) {
				double horizToVertRatio = config.yScale.sample(random);
				float radius = 1.0F + random.nextFloat() * 6.0F;
				this.createRoom(ctx, config, access, biomePos, aquifer, x, y, z, radius, horizToVertRatio, mask, checker);
				tunnelCount += random.nextInt(4);
			}

			for (int tunnelIndex = 0; tunnelIndex < tunnelCount; ++tunnelIndex) {
				float randomRadian = random.nextFloat() * ((float) Math.PI * 2F);
				float randomPitch = (random.nextFloat() - 0.5F) / 4.0F;
				float thickness = this.getThickness(random);
				int branchCount = i - random.nextInt(i / 4);

				this.createTunnel(ctx, config, access, biomePos, random.nextLong(), aquifer, x, y, z, horiz, vert, thickness, randomRadian, randomPitch, 0, branchCount, this.getYScale(), mask, checker);
			}
		}

		return true;
	}

	@Override
	protected boolean carveBlock(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomePos, CarvingMask mask, BlockPos.MutableBlockPos posMutable, BlockPos.MutableBlockPos posUp, Aquifer aquifer, MutableBoolean isSurface) {
		BlockPos pos = posMutable.immutable();
		BlockState blockstate = access.getBlockState(pos);
		if (blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.MYCELIUM) || blockstate.is(Blocks.PODZOL) || blockstate.is(Blocks.DIRT_PATH)) {
			isSurface.setTrue();
		}

		//We dont want caves to go so far down you can see bedrock, so lets stop them right before
		if (pos.getY() < access.getMinBuildHeight() + 6) return false;

		if (!this.canReplaceBlock(config, blockstate) && !isDebugEnabled(config)) {
			return false;
		} else {
			for (Direction facing : Direction.values())
                if (access.getFluidState(pos.relative(facing)).is(FluidTags.WATER))
                    return false; // If replacing this block will expose any neighboring water, then skip the current position param

			BlockState blockState = this.getCarveState(ctx, config, pos, aquifer);
            if (blockState != null) {
				RandomSource randomFromPos = ctx.randomState().oreRandom().at(pos);

				if (!access.getFluidState(pos.above(2)).isEmpty()) // Sand doesn't quite generate until after the carvers, so we must look for liquid instead
					blockState = randomFromPos.nextBoolean() ? Blocks.ROOTED_DIRT.defaultBlockState() : Blocks.COARSE_DIRT.defaultBlockState(); // normal dirt will get replaced with sand, special ones are required

                boolean blockPlaced = access.setBlockState(pos, blockState, false) == Blocks.STONE.defaultBlockState();

                if (aquifer.shouldScheduleFluidUpdate() && !blockState.getFluidState().isEmpty()) {
                    access.markPosForPostprocessing(pos);
                }

                if (isSurface.isTrue()) {
                    BlockPos posDown = pos.relative(Direction.DOWN);
                    if (access.getBlockState(posDown).is(Blocks.DIRT)) {
                        ctx.topMaterial(biomePos, access, posDown, !blockState.getFluidState().isEmpty()).ifPresent((state -> {
                            access.setBlockState(posDown, state, false);
                            if (!state.getFluidState().isEmpty()) {
                                access.markPosForPostprocessing(posDown);
                            }
                        }));
                    }
                }

                if (blockPlaced)
					return this.postCarveBlock(access, pos, config, randomFromPos);

                return true;
            } else {
                return false;
            }
        }
	}

	private boolean postCarveBlock(ChunkAccess access, BlockPos pos, CaveCarverConfiguration config, RandomSource rand) {
		for (Direction facing : Direction.values()) {
			BlockPos directionalRelative = pos.relative(facing);

			if (this.isHighlands) {
				if (rand.nextBoolean() && this.canReplaceBlock(config, access.getBlockState(directionalRelative))) {
					return access.setBlockState(directionalRelative, rand.nextInt(8) == 0 ? TFBlocks.TROLLSTEINN.get().defaultBlockState() : Blocks.STONE.defaultBlockState(), false) != null;
				}
			} else { //here's the code for making dirt roofs. Enjoy :)
				if (facing == Direction.DOWN)
					continue; // Dirt is never placed below, always on roof, and typically to the sides

                BlockState neighboringBlock = access.getBlockState(directionalRelative);

				if (neighboringBlock.is(BlockTags.BASE_STONE_OVERWORLD) || neighboringBlock.getFluidState().is(FluidTags.WATER)) {
					return switch (rand.nextInt(5)) {
						default -> access.setBlockState(directionalRelative, Blocks.DIRT.defaultBlockState(), false) != null;
						case 3 -> access.setBlockState(directionalRelative, Blocks.ROOTED_DIRT.defaultBlockState(), false) != null;
						case 4 -> access.setBlockState(directionalRelative, Blocks.COARSE_DIRT.defaultBlockState(), false) != null;
					};
				}
			}
		}

		return false;
	}

	protected int getCaveBound() {
		return 4;
	}

	protected float getThickness(RandomSource rand) {
		float f = rand.nextFloat() * 2.0F + rand.nextFloat();
		if (rand.nextInt(10) == 0) {
			f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double getYScale() {
		return 1.0D;
	}

	protected void createRoom(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomePos, Aquifer aquifer, double posX, double posY, double posZ, float radius, double horizToVertRatio, CarvingMask mask, CarveSkipChecker checker) {
		double d0 = 1.5D + (double) (Mth.sin(((float) Math.PI / 2F)) * radius);
		double d1 = d0 * horizToVertRatio;
		this.carveEllipsoid(ctx, config, access, biomePos, aquifer, posX, posY, posZ, d0, d1, mask, checker);
	}

	protected void createTunnel(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomePos, long seed, Aquifer aquifer, double posX, double posY, double posZ, double horizMult, double vertMult, float thickness, float yaw, float pitch, int branchIndex, int branchCount, double horizToVertRatio, CarvingMask mask, CarveSkipChecker checker) {
		RandomSource random = RandomSource.create(seed);
		int i = random.nextInt(branchCount / 2) + branchCount / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		for (int j = branchIndex; j < branchCount; ++j) {
			double horizontalRadius = 1.5D + (double) (Mth.sin((float) Math.PI * (float) j / (float) branchCount) * thickness);
			double verticalRadius = horizontalRadius * horizToVertRatio;
			float f2 = Mth.cos(pitch);
			posX += Mth.cos(yaw) * f2;

			float yShift = Mth.sin(pitch);
			// If posY nears bedrock, "slow" its descent if marching downwards
			posY += yShift > 0 || posY + yShift > access.getMinBuildHeight() + 10 ? yShift : yShift * 0.25f;

			posZ += Mth.sin(yaw) * f2;
			pitch = pitch * (flag ? 0.92F : 0.7F);
			pitch = pitch + f1 * 0.1F;
			yaw += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && thickness > 1.0F) {
				this.createTunnel(ctx, config, access, biomePos, random.nextLong(), aquifer, posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw - ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, mask, checker);
				this.createTunnel(ctx, config, access, biomePos, random.nextLong(), aquifer, posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw + ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, mask, checker);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!canReach(access.getPos(), posX, posZ, j, branchCount, thickness)) {
					return;
				}

				// Additional size-boosting to make wide spherical rooms
				boolean shouldEnlargeSphere = posY > access.getMinBuildHeight() + 12 && random.nextInt(32) == 0;
				float sizeMultiplier = shouldEnlargeSphere
						? random.nextFloat() * random.nextFloat() * 2f + 1
						: 1;

				double sphereHRadius = Math.min(horizontalRadius * horizMult * sizeMultiplier, 10);
				this.carveEllipsoid(ctx, config, access, biomePos, aquifer, posX, posY, posZ, sphereHRadius, Math.min(verticalRadius * vertMult * sizeMultiplier, sphereHRadius * 0.65f), mask, checker);
			}
		}

	}

	@Override
	protected boolean canReplaceBlock(CaveCarverConfiguration config, BlockState state) {
		return !state.is(BlockTags.ICE) && !state.getFluidState().is(FluidTags.WATER) && super.canReplaceBlock(config, state);
	}

	private static boolean shouldSkip(double posX, double posY, double posZ, double minY) {
		if (posY <= minY) {
			return true;
		} else {
			return posX * posX + posY * posY + posZ * posZ >= 1.0D;
		}
	}

	@Nullable
	@Override
	public BlockState getCarveState(CarvingContext pContext, CaveCarverConfiguration pConfig, BlockPos pPos, Aquifer pAquifer) {
        BlockState blockstate = pAquifer.computeSubstance(new DensityFunction.SinglePointContext(pPos.getX(), pPos.getY(), pPos.getZ()), 0);
        if (blockstate == null) {
            return null;
        } else {
            return Blocks.CAVE_AIR.defaultBlockState();
        }
    }
}
