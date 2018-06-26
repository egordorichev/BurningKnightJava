package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;

public class Equipable extends Accessory {
	public Player owner = Player.instance;
}