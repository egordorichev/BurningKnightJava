package org.rexcellentgames.burningknight.entity.level.entities.chest;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.pool.item.GoldChestPool;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class GoldenChest extends Chest {
	private static Animation animation = Animation.make("chest", "-golden");
	private static AnimationData closed = animation.get("closed");
	private static AnimationData open = animation.get("anim");
	private static AnimationData openend = animation.get("open");

	@Override
	public Item generate() {
		return weapon ? GoldChestPool.weapons.generate() : GoldChestPool.items.generate();
	}

	@Override
	protected AnimationData getClosedAnim() {
		return closed;
	}

	@Override
	protected AnimationData getOpenAnim() {
		return open;
	}

	@Override
	protected AnimationData getOpenedAnim() {
		return openend;
	}
}