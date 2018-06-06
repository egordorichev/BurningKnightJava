package org.rexellentgames.dungeon.entity.item.pet.orbital;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.pet.Pet;
import org.rexellentgames.dungeon.entity.item.pet.impl.Orbital;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;

public class GooOrbital extends Pet {
	{
		name = Locale.get("goo");
		description = Locale.get("goo_desc");
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-goo";
		}
	}
}