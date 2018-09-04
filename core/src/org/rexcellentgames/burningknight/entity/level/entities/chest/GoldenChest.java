package org.rexcellentgames.burningknight.entity.level.entities.chest;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class GoldenChest extends Chest {
	public static Animation animation = Animation.make("chest", "-golden");
	private static AnimationData closed = animation.get("idle");
	private static AnimationData open = animation.get("opening");
	private static AnimationData openend = animation.get("open");

	@Override
	protected Animation getAnim() {
		return animation;
	}

	@Override
	public Item generate() {
		return Chest.generate(ItemRegistry.Quality.GOLDEN, weapon);
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

	@Override
	public void open() {
		super.open();

		if (Random.chance(70)) {
			for (int i = 0; i < Random.newInt(2, 5); i++) {
				HeartFx fx = new HeartFx();

				fx.x = this.x + (this.w - fx.w) / 2;
				fx.y = this.y + (this.h - fx.h) / 2;

				Dungeon.area.add(fx);
			}
		}

	}
}