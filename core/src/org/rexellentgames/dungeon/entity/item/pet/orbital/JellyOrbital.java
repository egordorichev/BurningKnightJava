package org.rexellentgames.dungeon.entity.item.pet.orbital;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.pet.Pet;
import org.rexellentgames.dungeon.entity.item.pet.impl.Orbital;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;

public class JellyOrbital extends Pet {
	{
		name = Locale.get("jelly");
		description = Locale.get("jelly_desc");
		sprite = "item-jelly";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-jelly";
		}
	}
}