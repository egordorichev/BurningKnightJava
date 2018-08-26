package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class ChallengeRune extends Equipable {
	{
		name = Locale.get("challenge_rune");
		description = Locale.get("challenge_rune_desc");
		sprite = "item-scroll_d";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		Mob.challenge = true;

		for (Mob mob : Mob.all) {
			mob.generatePrefix();
		}

		this.owner.modifyDefense(2);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		Mob.challenge = false;
		this.owner.modifyDefense(-2);
	}
}