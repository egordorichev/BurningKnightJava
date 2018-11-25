package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
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

			if ((entity instanceof Projectile && ((Projectile) entity).bad && Random.chance(20))) {
				this.done = true;

				for (int i = 0; i < 10; i++) {
					PoofFx fx = new PoofFx();

					fx.x = this.x + this.w / 2;
					fx.y = this.y + this.h / 2;

					Dungeon.area.add(fx);
				}

				PlayerSave.remove(this);
			}
		}
	}
}