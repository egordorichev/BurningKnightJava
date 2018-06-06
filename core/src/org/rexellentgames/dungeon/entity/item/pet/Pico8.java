package org.rexellentgames.dungeon.entity.item.pet;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.item.pet.impl.SimpleFollowPet;

public class Pico8 extends Pet {
	{
		name = Locale.get("pico8");
		description = Locale.get("pico8_desc");
		sprite = "item-pico8";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends SimpleFollowPet {
		{
			sprite = "item-pico8";
		}
	}
}