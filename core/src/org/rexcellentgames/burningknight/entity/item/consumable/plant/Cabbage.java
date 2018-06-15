package org.rexcellentgames.burningknight.entity.item.consumable.plant;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.Tween;

public class Cabbage extends Plant {
	private static final int HUNGER = 50;

	{
		name = "Cabbage";
		description = "Tastes good, probably will sell good too.";
		sprite = "veggie-cabbage-growth-02";
		useTime = 10f;
		identified = true;
	}

	@Override
	public void use() {
		super.use();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			player.setHunger(player.getHunger() - HUNGER);
			player.modifyHp(2, null);
		}

		Tween.to(new Tween.Task(-70, 0.3f) {
			@Override
			public float getValue() {
				return added;
			}

			@Override
			public void setValue(float value) {
				added = value;
			}

			@Override
			public void onEnd() {
				count -= 1;
				added = 0;
			}
		});
	}
}