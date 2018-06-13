package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class ChallengeRune extends Equipable {
	{
		name = Locale.get("challenge_rune");
		description = Locale.get("challenge_rune_desc");
		sprite = "item (scroll D)";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		Mob.challenge = true;

		for (Mob mob : Mob.all) {
			mob.generatePrefix();
		}

		this.owner.modifyDefense(2);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		Mob.challenge = false;
		this.owner.modifyDefense(-2);
	}
}