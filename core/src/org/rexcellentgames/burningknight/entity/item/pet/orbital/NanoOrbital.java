package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;

public class NanoOrbital extends Pet {
	{
		name = Locale.get("nano_orbital");
		description = Locale.get("nano_orbital_desc");
		sprite = "item-mini_orbital";
	}

	@Override
	public PetEntity create() {
		return new Impl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-mini_orbital";
		}
	}
}