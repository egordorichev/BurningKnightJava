package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;

public class SwordOrbital extends Pet {
	{
		name = Locale.get("sword_orbital");
		description = Locale.get("sword_orbital_desc");
		sprite = "item-sword_orbital";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-sword_orbital";
		}

		@Override
		public void onCollision(Entity entity) {
			super.onCollision(entity);

			if (entity instanceof Mob) {
				((Mob) entity).modifyHp(-3, this.owner);
				((Mob) entity).knockBackFrom(this, 1000f);
			}
		}
	}
}