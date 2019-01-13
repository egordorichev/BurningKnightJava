package org.rexcellentgames.burningknight.entity.creature.mob.common;

import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class CoinMan extends SupplyMan {
	public static Animation animations = Animation.make("actor-supply", "-coin");

	@Override
	public Animation getAnimation() {
		return animations;
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = new ArrayList<>();

		for (int i = 0; i < Random.newInt(8, 20); i++) {
			items.add(new Gold());
		}

		return items;
	}
}