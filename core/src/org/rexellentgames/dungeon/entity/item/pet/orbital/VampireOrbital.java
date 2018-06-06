package org.rexellentgames.dungeon.entity.item.pet.orbital;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.entity.item.pet.Pet;
import org.rexellentgames.dungeon.entity.item.pet.impl.Orbital;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.ArrowEntity;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.util.Random;

public class VampireOrbital extends Pet {
	{
		name = Locale.get("vampire_orbital");
		description = Locale.get("vampire_orbital_desc");
	}

	@Override
	public PetEntity create() {
		return new BrokenOrbital.Impl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-vamprite_orbital";
		}

		@Override
		protected void onHit(Entity entity) {
			super.onHit(entity);

			if (entity instanceof BulletEntity || entity instanceof ArrowEntity || entity instanceof Fireball) {
				if (Random.chance(5)) {
					this.owner.modifyHp(2, null);
				}
			}
		}
	}
}