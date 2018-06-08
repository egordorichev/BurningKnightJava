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
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Part;
import org.rexellentgames.dungeon.entity.level.save.PlayerSave;
import org.rexellentgames.dungeon.util.Random;

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

			if (entity instanceof BulletEntity || entity instanceof ArrowEntity || entity instanceof Fireball) {
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