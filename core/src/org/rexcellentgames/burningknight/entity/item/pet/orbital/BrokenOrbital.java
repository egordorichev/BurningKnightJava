package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.entity.item.weapon.projectile.FireballProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.ArrowProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.util.Random;

public class BrokenOrbital extends Pet {
	{
		name = Locale.get("broken_orbital");
		description = Locale.get("broken_orbital_desc");
		sprite = "item-broken_orbital";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-broken_orbital";
		}

		@Override
		protected void onHit(Entity entity) {
			super.onHit(entity);

			if (entity instanceof BulletProjectile || entity instanceof ArrowProjectile || entity instanceof FireballProjectile) {
				this.done = true;

				for (int i = 0; i < 20; i++) {
					Part part = new Part();

					part.x = this.x + Random.newFloat(this.w);
					part.y = this.y + Random.newFloat(this.h);

					Dungeon.area.add(part);
				}

				PlayerSave.remove(this);
			}
		}
	}
}