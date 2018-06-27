package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.entity.item.weapon.projectile.FireballProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.ArrowProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.util.Random;

public class VampireOrbital extends Pet {
	{
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			
		}

		@Override
		protected void onHit(Entity entity) {
			super.onHit(entity);

			if (entity instanceof BulletProjectile || entity instanceof ArrowProjectile || entity instanceof FireballProjectile) {
				if (Random.chance(10)) {
					this.owner.modifyHp(2, null);
				}
			}
		}
	}
}