package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

public class TFDataSerializers {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, TwilightForestMod.ID);

	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<String>>> STRING_LIST = DATA_SERIALIZERS.register("string_list", () -> new EntityDataSerializer.ForValueType<>() {
		@Override
		public void write(FriendlyByteBuf buf, List<String> strings) {
			buf.writeCollection(strings, FriendlyByteBuf::writeUtf);
		}

		@Override
		public List<String> read(FriendlyByteBuf buf) {
			return buf.readList(FriendlyByteBuf::readUtf);
		}

		@Override
		public List<String> copy(List<String> strings) {
			return new ArrayList<>(strings);
		}
	});
}
