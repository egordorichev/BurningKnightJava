package org.rexellentgames.dungeon.entity.item.weapon.magic;

import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.fx.IchorFx;
import org.rexellentgames.dungeon.game.input.Input;

public class DefenseBook extends MagicWeapon {
	{
		name = Locale.get("defense_book");
		description = Locale.get("defense_book_desc");
		damage = 0;
		mana = 5;
		useTime = 0.1f;
	}

	@Override
	public void use() {
		super.use();

		if (this.delay != 0) {
			IchorFx fx = new IchorFx();

			float dx = Input.instance.worldMouse.x - this.owner.x - this.owner.w / 2;
			float dy = Input.instance.worldMouse.y - this.owner.h / 2 - this.owner.y;
			float a = (float) Math.atan2(dy, dx);

			fx.x = (float) (this.owner.x + (this.owner.w - 8) / 2 + Math.cos(a) * 8);
			fx.y = (float) (this.owner.y + (this.owner.h - 8) / 2 + Math.sin(a) * 8);
			fx.vel = new Vector2((float) Math.cos(a) * 60f, (float) Math.sin(a) * 60f);


			Dungeon.area.add(fx);
		}
	}
}