package org.rexellentgames.dungeon.entity.item.pet;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.item.pet.impl.BumboPet;

public class Bumbo extends Pet {
	{
		name = Locale.get("bumbo");
		description = Locale.get("bumbo_desc");
	}

	@Override
	public PetEntity create() {
		return new BumboPet();
	}
}