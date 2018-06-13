package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.entity.creature.fx.Fireball;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.fx.Fireball;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BulletEntity;
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
			super.onHit(entity);

			if (entity instanceof BulletEntity || entity instanceof ArrowEntity || entity instanceof Fireball) {
				if (Random.chance(10)) {
					this.owner.modifyHp(2, null);
				}
			}
		}
	}
}