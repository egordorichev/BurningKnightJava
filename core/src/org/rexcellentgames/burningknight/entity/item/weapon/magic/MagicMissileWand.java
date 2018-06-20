package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.FireballProjectile;

public class MagicMissileWand extends Wand {
	{
		name = Locale.get("magic_missile_wand");
		description = Locale.get("magic_missile_wand_desc");
		sprite = "item (wand A)";
	}

	@Override
	public void spawnProjectile(float x, float y, float a) {
		FireballProjectile fireball = new FireballProjectile();

		fireball.owner = this.owner;
		fireball.x = x;
		fireball.y = y;

		double ra = Math.toRadians(a);

		fireball.vel.x = (float) Math.cos(ra) * 60;
		fireball.vel.y = (float) Math.sin(ra) * 60;

		Dungeon.area.add(fireball);
	}
}