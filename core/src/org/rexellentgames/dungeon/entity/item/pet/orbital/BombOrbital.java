package org.rexellentgames.dungeon.entity.item.pet.orbital;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Bomb;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.pet.Pet;
import org.rexellentgames.dungeon.entity.item.pet.impl.Orbital;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.util.Random;

public class BombOrbital extends Pet {
	{
		name = Locale.get("bomb_orbital");
		description = Locale.get("bomb_orbital_desc");
		sprite = "item-bomb_orbital";
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
			sprite = "item-bomb_orbital";
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