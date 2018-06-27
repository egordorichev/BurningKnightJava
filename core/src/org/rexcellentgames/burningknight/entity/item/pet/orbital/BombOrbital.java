package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.Dungeon;

public class BombOrbital extends Pet {
	{
	}

	@Override
	public void use() {
		super.use();

		ItemHolder item = new ItemHolder();
		item.setItem(new Bomb());
		item.getItem().setCount(5);

		Dungeon.area.add(item);

		item.x = Player.instance.x + (Player.instance.w - item.w) / 2;
		item.y = Player.instance.y + (Player.instance.h - item.h) / 2;
		item.getBody().setTransform(item.x, item.y, 0);
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			
		}

		private float last;

		@Override
		public void init() {
			super.init();
			last = Random.newFloat(60);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			last += dt;

			if (last >= 60f) {
				last = 0;

				ItemHolder item = new ItemHolder();
				item.setItem(new Bomb());
				Dungeon.area.add(item);

				item.x = this.x + (this.w - item.w) / 2;
				item.y = this.y + (this.h - item.h) / 2;
				item.getBody().setTransform(item.x, item.y, 0);
			}
		}
	}
}