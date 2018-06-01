package org.rexellentgames.dungeon.entity.item.pet.impl;

import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;

public class PetEntity extends SaveableEntity {
	public Player owner;

	@Override
	public void init() {
		super.init();

		this.owner = Player.instance;
	}
}