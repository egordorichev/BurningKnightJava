package org.rexcellentgames.burningknight.entity.item.consumable.food;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.util.Tween;

public class Pill extends Consumable {
	@Override
	public void use() {
		super.use();

		Dungeon.glitchTime = 6;
		Player.instance.modifyHp(Player.instance.getHpMax(), null);

		Tween.to(new Tween.Task(0.3f, 0.3f) {
			@Override
			public float getValue() {
				return Dungeon.speed;
			}

			@Override
			public void setValue(float value) {
				Dungeon.speed = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(1f, 0.3f) {
					@Override
					public float getValue() {
						return Dungeon.speed;
					}

					@Override
					public void setValue(float value) {
						Dungeon.speed = value;
					}
				}).delay(5);
			}
		});
	}

	@Override
	public boolean canBeUsed() {
		return super.canBeUsed() && Player.instance.getHp() < Player.instance.getHpMax();
	}
}