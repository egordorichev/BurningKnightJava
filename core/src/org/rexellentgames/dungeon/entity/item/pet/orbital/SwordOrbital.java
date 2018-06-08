package org.rexellentgames.dungeon.entity.item.pet.orbital;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.pet.Pet;
import org.rexellentgames.dungeon.entity.item.pet.impl.Orbital;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;

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
				((Mob) entity).knockBack(this, 1000f);
			}
		}
	}
}