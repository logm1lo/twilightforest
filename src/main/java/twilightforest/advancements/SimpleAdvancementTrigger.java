package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import twilightforest.init.TFAdvancementTriggers;

import java.util.Optional;

public class SimpleAdvancementTrigger extends SimpleCriterionTrigger<SimpleAdvancementTrigger.TriggerInstance> {

	@Override
	public Codec<SimpleAdvancementTrigger.TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, (instance) -> true);
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {

		public static final Codec<SimpleAdvancementTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(SimpleAdvancementTrigger.TriggerInstance::player))
				.apply(instance, SimpleAdvancementTrigger.TriggerInstance::new));

		public static Criterion<SimpleAdvancementTrigger.TriggerInstance> makeTFPortal() {
			return TFAdvancementTriggers.MADE_TF_PORTAL.get().createCriterion(new TriggerInstance(Optional.empty()));
		}

		public static Criterion<SimpleAdvancementTrigger.TriggerInstance> eatHydraChop() {
			return TFAdvancementTriggers.CONSUME_HYDRA_CHOP.get().createCriterion(new TriggerInstance(Optional.empty()));
		}

		public static Criterion<SimpleAdvancementTrigger.TriggerInstance> completeQuestRam() {
			return TFAdvancementTriggers.QUEST_RAM_COMPLETED.get().createCriterion(new TriggerInstance(Optional.empty()));
		}

		public static Criterion<SimpleAdvancementTrigger.TriggerInstance> activateGhastTrap() {
			return TFAdvancementTriggers.ACTIVATED_GHAST_TRAP.get().createCriterion(new TriggerInstance(Optional.empty()));
		}

		public static Criterion<SimpleAdvancementTrigger.TriggerInstance> killAllPhantoms() {
			return TFAdvancementTriggers.KILL_ALL_PHANTOMS.get().createCriterion(new TriggerInstance(Optional.empty()));
		}

		public static Criterion<SimpleAdvancementTrigger.TriggerInstance> activatedPedestal() {
			return TFAdvancementTriggers.PLACED_TROPHY_ON_PEDESTAL.get().createCriterion(new TriggerInstance(Optional.empty()));
		}
	}
}
