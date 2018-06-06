package org.rexellentgames.dungeon.entity.item.pet;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.item.pet.impl.SimpleFollowPet;

public class StrawberryPet extends Pet {
	{
		name = Locale.get("celeste");
		description = Locale.get("celeste_desc");
		sprite = "item-strawberry";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends SimpleFollowPet {
		{
			sprite = "item-strawberry";
		}
	}
}