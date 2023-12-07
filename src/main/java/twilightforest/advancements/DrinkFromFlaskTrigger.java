package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.alchemy.Potion;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

public class DrinkFromFlaskTrigger extends SimpleCriterionTrigger<DrinkFromFlaskTrigger.TriggerInstance> {

	@Override
	public Codec<DrinkFromFlaskTrigger.TriggerInstance> codec() {
		return DrinkFromFlaskTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, int doses, Potion potion) {
		this.trigger(player, (instance) -> instance.matches(doses, potion));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints doses, Optional<Potion> potion) implements SimpleInstance {

		public static final Codec<DrinkFromFlaskTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(DrinkFromFlaskTrigger.TriggerInstance::player),
				ExtraCodecs.strictOptionalField(MinMaxBounds.Ints.CODEC, "doses", MinMaxBounds.Ints.between(0, 4)).forGetter(DrinkFromFlaskTrigger.TriggerInstance::doses),
				BuiltInRegistries.POTION.byNameCodec().optionalFieldOf("potion").forGetter(DrinkFromFlaskTrigger.TriggerInstance::potion))
				.apply(instance, DrinkFromFlaskTrigger.TriggerInstance::new));

		public boolean matches(int doses, Potion potion) {
			return this.doses.matches(doses) && this.potion.isPresent() && this.potion.get() == potion;
		}

		public static Criterion<DrinkFromFlaskTrigger.TriggerInstance> drankPotion(int doses, Potion potion) {
			return TFAdvancements.DRINK_FROM_FLASK.get().createCriterion(new TriggerInstance(Optional.empty(), MinMaxBounds.Ints.exactly(doses), Optional.of(potion)));
		}
	}
}
