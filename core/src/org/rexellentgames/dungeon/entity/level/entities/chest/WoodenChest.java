package org.rexellentgames.dungeon.entity.level.entities.chest;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.pool.item.WoodenChestPool;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;

public class WoodenChest extends Chest {
	private static Animation animation = Animation.make("actor-wooden-chest");
	private static AnimationData closed = animation.get("closed");
	private static AnimationData open = animation.get("anim");
	private static AnimationData openend = animation.get("open");

	@Override
	public Item generate() {
		return WoodenChestPool.instance.generate();
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