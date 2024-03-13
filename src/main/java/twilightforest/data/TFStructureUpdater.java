package twilightforest.data;

import com.google.common.hash.Hashing;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerUpper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Code borrowed from <a href="https://github.com/BluSunrize/ImmersiveEngineering/blob/1.20.4/src/datagen/java/blusunrize/immersiveengineering/data/StructureUpdater.java">Immersive Enginnering</a> <br>
 * Note: when datagen is run for the first time after an update, a copy of each structure file will be moved to the generated folder. Simply move them over to main. This won't run if the files are already up-to-date.
 */
public class TFStructureUpdater implements DataProvider {
	private final String basePath;
	private final PackOutput output;
	private final MultiPackResourceManager resources;

	public TFStructureUpdater(String basePath, PackOutput output, ExistingFileHelper helper) {
		this.basePath = basePath;
		this.output = output;
		try {
			Field serverData = ExistingFileHelper.class.getDeclaredField("serverData");
			serverData.setAccessible(true);
			this.resources = (MultiPackResourceManager) serverData.get(helper);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
		try {
			for (var entry : this.resources.listResources(this.basePath, $ -> true).entrySet())
				if (entry.getKey().getNamespace().equals(TwilightForestMod.ID))
					process(entry.getKey(), entry.getValue(), cache);
			return CompletableFuture.completedFuture(null);
		} catch (IOException x) {
			return CompletableFuture.failedFuture(x);
		}
	}

	private void process(ResourceLocation loc, Resource resource, CachedOutput cache) throws IOException {
		CompoundTag inputNBT = NbtIo.readCompressed(resource.open(), NbtAccounter.unlimitedHeap());
		CompoundTag converted = updateNBT(inputNBT);
		if (!converted.equals(inputNBT)) {
			Class<? extends DataFixer> fixerClass = DataFixers.getDataFixer().getClass();
			if (!fixerClass.equals(DataFixerUpper.class))
				throw new RuntimeException("Structures are not up to date, but unknown data fixer is in use: " + fixerClass.getName());
			writeNBTTo(loc, converted, cache);
		}
	}

	private void writeNBTTo(ResourceLocation loc, CompoundTag data, CachedOutput cache) throws IOException {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		NbtIo.writeCompressed(data, bytearrayoutputstream);
		byte[] bytes = bytearrayoutputstream.toByteArray();
		Path outputPath = this.output.getOutputFolder().resolve("data/" + loc.getNamespace() + "/" + loc.getPath());
		cache.writeIfNeeded(outputPath, bytes, Hashing.sha1().hashBytes(bytes));
	}

	private static CompoundTag updateNBT(CompoundTag nbt) {
		final CompoundTag updatedNBT = DataFixTypes.STRUCTURE.updateToCurrentVersion(
				DataFixers.getDataFixer(), nbt, nbt.getInt("DataVersion")
		);
		StructureTemplate template = new StructureTemplate();
		template.load(BuiltInRegistries.BLOCK.asLookup(), updatedNBT);
		return template.save(new CompoundTag());
	}

	@Nonnull
	@Override
	public String getName() {
		return "Update structure files in " + this.basePath;
	}
}
