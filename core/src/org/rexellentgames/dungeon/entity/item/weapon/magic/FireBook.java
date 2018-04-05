package org.rexellentgames.dungeon.entity.item.weapon.magic;

import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.game.input.Input;

public class FireBook extends MagicWeapon {
	{
		name = "Fire Book";
		description = "OH MY GOSH!";
		sprite = "item (wand C)";
		damage = 6;
		mana = 10;
		useTime = 0.2f;
	}

	@Override
	public void use() {
		super.use();

		if (this.delay != 0) {
			Fireball ball = new Fireball();
			float dx = Input.instance.worldMouse.x - this.owner.x - this.owner.w / 2;
			float dy = Input.instance.worldMouse.y - this.owner.h / 2 - this.owner.y;
			double a = Math.atan2(dy, dx);

			ball.bad = false;
			ball.toMouse = true;

			ball.x = this.owner.x + (this.owner.w - 16) / 2;
			ball.y = this.owner.y + (this.owner.h - 16) / 2;
			ball.vel = new Vector2((float) Math.cos(a) * 60f, (float) Math.sin(a) * 60f);

			Dungeon.area.add(ball);
		}
	}
}