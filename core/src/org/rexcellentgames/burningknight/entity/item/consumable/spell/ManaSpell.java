package org.rexcellentgames.burningknight.entity.item.consumable.spell;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

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