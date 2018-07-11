package org.rexcellentgames.burningknight.entity.level.entities.chest;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.pool.item.IronChestPool;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class IronChest extends Chest {
	public static Animation animation = Animation.make("chest", "-iron");
	private static AnimationData closed = animation.get("idle");
	private static AnimationData open = animation.get("opening");
	private static AnimationData openend = animation.get("open");

	@Override
	public Item generate() {
		return weapon ? IronChestPool.weapons.generate() : IronChestPool.items.generate();
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