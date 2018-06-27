package org.rexcellentgames.burningknight.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class DeathShotPrefix extends Prefix {
	private static Color color = Color.valueOf("#ff00ff");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void onDeath(Mob mob) {
		super.onDeath(mob);

		for (int i = 0; i < 8; i++) {
			BulletProjectile ball = new BulletProjectile();

			float a = (float) (i * Math.PI / 4);
			ball.vel = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(60f * Mob.shotSpeedMod);

			ball.x = (float) (mob.x + mob.w / 2 + Math.cos(a) * 8);
			ball.y = (float) (mob.y + Math.sin(a) * 8 + 6);
			ball.damage = 2;

			ball.letter = "bad";
			Dungeon.area.add(ball);
		}
	}
}