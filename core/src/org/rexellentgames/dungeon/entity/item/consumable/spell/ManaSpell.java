package org.rexellentgames.dungeon.entity.item.consumable.spell;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class ManaSpell extends Spell {
	{
		name = Locale.get("mana_spell");
		description = Locale.get("mana_spell_desc");
	}

	@Override
	public void use() {
		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			if (player.getMana() == player.getManaMax()) {
				return;
			}

			super.use();

			player.modifyMana(player.getManaMax() - player.getMana());
		}
	}
}