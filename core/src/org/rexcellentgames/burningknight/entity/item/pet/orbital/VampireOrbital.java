package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.FireballProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.util.Random;

public class VampireOrbital extends Pet {
	{
		name = Locale.get("vampire_orbital");
		description = Locale.get("vampire_orbital_desc");
		sprite = "item-vamprite_orbital";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-vamprite_orbital";
		}

		@Override
		protected void onHit(Entity entity) {
			if (entity instanceof Projectile && ((Projectile) entity).bad) {
				if (Random.chance(80)) {
					this.owner.modifyHp(2, null);
				}
			}

			super.onHit(entity);
		}
	}
}