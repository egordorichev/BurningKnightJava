package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.entity.creature.fx.Fireball;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.fx.Fireball;
import org.rexcellentgames.burningknight.game.input.Input;

public class FireBook extends MagicWeapon {
	{
		name = Locale.get("fire_book");
		description = Locale.get("fire_book_desc");
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

			ball.x = (float) (this.owner.x + (this.owner.w - 16) / 2 + Math.cos(a) * 8);
			ball.y = (float) (this.owner.y + (this.owner.h - 16) / 2 + Math.sin(a) * 8);
			ball.vel = new Vector2((float) Math.cos(a), (float) Math.sin(a));
			ball.owner = this.owner;

			Dungeon.area.add(ball);
		}
	}
}