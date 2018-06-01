package org.rexellentgames.dungeon.entity.item.pet;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.item.pet.impl.SimpleFollowPet;

public class StrawberryPet extends Pet {
	@Override
	public PetEntity create() {
		return new SimpleFollowPet();
	}
}