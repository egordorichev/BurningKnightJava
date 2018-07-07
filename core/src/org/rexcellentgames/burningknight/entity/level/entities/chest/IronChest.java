package org.rexcellentgames.burningknight.entity.level.entities.chest;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.pool.item.IronChestPool;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class IronChest extends Chest {
	private static Animation animation = Animation.make("chest", "-iron");
	private static AnimationData closed = animation.get("closed");
	private static AnimationData open = animation.get("anim");
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