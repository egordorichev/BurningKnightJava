package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class CobaltShield extends Equipable {
	{
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("knockback", -1);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("knockback", 1);
	}
}