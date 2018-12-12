package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.Dungeon;

public class AmmoOrbital extends Pet {
	{
		name = Locale.get("ammo_orbital");
		description = Locale.get("ammo_orbital_desc");
		sprite = "item-ammo_orbital";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-ammo_orbital";
		}

		private float last;

		@Override
		public void init() {
			super.init();

			last = Random.newFloat(3);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			this.last += dt;

			if (this.last > 3f) {
				this.last = 0;

				if (Player.instance.room != null) {
					for (Mob mob : Mob.every) {
						if (mob.onScreen && !mob.friendly && mob.room == this.owner.room && !mob.getState().equals("unactive") && !mob.getState().equals("defeated")) {
							BulletProjectile ball = new BulletProjectile();
							playSfx("gun_machinegun");

							float a = this.getAngleTo(mob.x + mob.w / 2, mob.y + mob.h / 2);
							ball.velocity = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(60f * 5f);

							ball.x = (float) (this.x + this.w / 2 + Math.cos(a) * 8);
							ball.damage = 4;
							ball.y = (float) (this.y + Math.sin(a) * 8 + 6);

							ball.letter = "a";
							Dungeon.area.add(ball);
							break;
						}
					}
				}
			}
		}
	}
}