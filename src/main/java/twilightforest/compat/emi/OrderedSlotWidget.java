package twilightforest.compat.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import twilightforest.TwilightForestMod;

import java.util.List;

public class OrderedSlotWidget extends SlotWidget {

	private long lastGenerate = 0L;
	private final List<EmiStack> list;
	private EmiIngredient stack = null;
	private long cycleTime;

	public OrderedSlotWidget(List<EmiStack> stacks, int x, int y, long cycleTime) {
		super(EmiStack.EMPTY, x, y);
		this.list = stacks;
		this.cycleTime = cycleTime;
	}

	@Override
	public EmiIngredient getStack() {
		long time = System.currentTimeMillis() / this.cycleTime;
		if (this.stack == null || time > this.lastGenerate) {
			this.lastGenerate = time;
			int index = (int) (this.lastGenerate % this.list.size());
			this.stack = this.list.get(index);
			TwilightForestMod.LOGGER.debug(this.list.size());
		}

		return this.stack;
	}
}
