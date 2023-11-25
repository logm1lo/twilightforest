package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.type.jigsaw.IndexedStructurePoolElement;

import java.util.function.Supplier;

public class TFStructureElementTypes {
	public static final DeferredRegister<StructurePoolElementType<?>> STRUCTURE_ELEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, TwilightForestMod.ID);

	public static final DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<IndexedStructurePoolElement>> INDEXED_ELEMENT = registerElement("indexed_element", () -> () -> IndexedStructurePoolElement.CODEC);

    private static <P extends StructurePoolElement> DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<P>> registerElement(String name, Supplier<StructurePoolElementType<P>> factory) {
        return STRUCTURE_ELEMENT_TYPES.register(name, factory);
    }
}
