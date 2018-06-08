package org.rexellentgames.dungeon.entity.item.pet.orbital;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.pet.Pet;
import org.rexellentgames.dungeon.entity.item.pet.impl.Orbital;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.util.geometry.Point;

public class AmmoOrbital extends Pet {
	{
		name = Locale.get("ammo_orbital");
		description = Locale.get("ammo_orbital_desc");
		sprite = "item-mini_orbital";
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
		public void update(float dt) {
			super.update(dt);

			this.last += dt;

			if (this.last > 3f) {
				this.last = 0;

				if (Player.instance.currentRoom != null) {
					for (Mob mob : Mob.all) {
						if (mob.onScreen) {
							BulletEntity ball = new BulletEntity();

							float a = this.getAngleTo(mob.x + mob.w / 2, mob.y + mob.h / 2);
							ball.vel = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(60f * 5f);

							ball.x = (float) (this.x + this.w / 2 + Math.cos(a) * 8);
							ball.damage = 4;
							ball.y = (float) (this.y + Math.sin(a) * 8 + 6);

							ball.letter = "A";
							Dungeon.area.add(ball);
							break;
						}
					}
				}
			}
		}
	}
}