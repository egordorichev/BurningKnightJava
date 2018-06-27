package org.rexcellentgames.burningknight.entity.item.pet;

import org.rexcellentgames.burningknight.entity.item.pet.impl.BumboPet;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.pet.impl.BumboPet;

public class Bumbo extends Pet {
	{
	}

	@Override
	public PetEntity create() {
		return new BumboPet();
	}
}