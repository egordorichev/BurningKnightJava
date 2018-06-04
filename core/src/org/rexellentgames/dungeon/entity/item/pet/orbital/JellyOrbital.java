package org.rexellentgames.dungeon.entity.item.pet.orbital;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.pet.Pet;
import org.rexellentgames.dungeon.entity.item.pet.impl.JellyOrbitalImpl;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;

public class JellyOrbital extends Pet {
	{
		name = Locale.get("jelly");
		description = Locale.get("jelly_desc");
	}

	// fixme : doesnt save pets

	public JellyOrbital() {
		this.owner = Player.instance;
	}

	@Override
	public PetEntity create() {
		return new JellyOrbitalImpl();
	}
}