package org.rexellentgames.dungeon.entity.item.consumable.food;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;
import org.rexellentgames.dungeon.util.Tween;

public class Food extends Consumable {
	protected float added;
	protected int hunger;

	{
		useTime = 10f;
		hunger = 10;
		identified = true;
	}

	@Override
	public void render(float x, float y, boolean flipped) {
		if (this.added != 0) {
			float angle = (flipped ? this.added : -this.added);

			Graphics.render(this.region, x + (flipped ? -3 : 3), y - 4, angle, 8, 10, false,
				false);
		}
	}

	@Override
	public void use() {
		super.use();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;
			player.setHunger(player.getHunger() - this.hunger);
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