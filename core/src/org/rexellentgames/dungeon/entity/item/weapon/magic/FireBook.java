package org.rexellentgames.dungeon.entity.item.weapon.magic;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.game.input.Input;

public class FireBook extends MagicWeapon {
	{
		name = "Fire Book";
		description = "OH MY GOSH!";
		sprite = "item (iron sword)";
		damage = 6;
		mana = 10;
		useTime = 0.2f;
	}

	@Override
	public void use() {
		super.use();

		if (this.delay != 0) {
			Fireball ball = new Fireball();

			ball.bad = false;
			ball.toMouse = true;

			ball.x = this.owner.x + (this.owner.w - 16) / 2;
			ball.y = this.owner.y + (this.owner.h - 16) / 2;

			Dungeon.area.add(ball);
		}
	}
}