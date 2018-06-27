package org.rexcellentgames.burningknight.entity.item.pet;

import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.pet.impl.SimpleFollowPet;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.pet.impl.SimpleFollowPet;

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