package org.rexellentgames.dungeon.entity.item.pet;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.item.pet.impl.SimpleFollowPet;

public class LibGDX extends Pet {
	{
		name = Locale.get("libgdx");
		description = Locale.get("libgdx_desc");
		sprite = "item-libgdx";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends SimpleFollowPet {
		{
			sprite = "item-libgdx_big";
		}
	}
}