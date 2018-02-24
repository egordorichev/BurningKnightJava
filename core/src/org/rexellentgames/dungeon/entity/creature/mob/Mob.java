package org.rexellentgames.dungeon.entity.creature.mob;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class Mob extends Creature {
	protected Creature target;

	protected void assignTarget() {
		this.target = (Creature) this.area.getRandomEntity(Player.class);
	}
}