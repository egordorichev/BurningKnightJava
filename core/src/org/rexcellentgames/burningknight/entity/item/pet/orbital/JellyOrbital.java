package org.rexcellentgames.burningknight.entity.item.pet.orbital;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.pet.Pet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.JellyOrbitalImpl;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;

public class JellyOrbital extends Pet {
	{
		name = Locale.get("jelly");
		description = Locale.get("jelly_desc");
		sprite = "item-jelly";
	}


	@Override
	public PetEntity create() {
		return new JellyOrbitalImpl();
	}

	public static class Impl extends Orbital {
		{
			sprite = "item-jelly";
		}
	}
}