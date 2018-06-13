package org.rexcellentgames.burningknight.entity.item.consumable.spell;

import org.rexcellentgames.burningknight.entity.creature.buff.InvisibilityBuff;
import org.rexcellentgames.burningknight.entity.creature.player.GhostPlayer;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.InvisibilityBuff;
import org.rexcellentgames.burningknight.entity.creature.player.GhostPlayer;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

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