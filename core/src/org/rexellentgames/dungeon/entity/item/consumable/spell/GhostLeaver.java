package org.rexellentgames.dungeon.entity.item.consumable.spell;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.InvisibilityBuff;
import org.rexellentgames.dungeon.entity.creature.player.GhostPlayer;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class GhostLeaver extends Spell {
	{
		name = Locale.get("ghost_leaver");
		description = Locale.get("ghost_leaver_desc");
	}

	@Override
	public void use() {
		super.use();

		Player.instance.addBuff(new InvisibilityBuff().setDuration(1000000000));

		Player player = new GhostPlayer();
		Dungeon.area.add(player);
	}
}