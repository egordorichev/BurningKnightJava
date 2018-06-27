package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class BloodCrown extends Equipable {
	{
		name = Locale.get("blood_crown");
		description = Locale.get("blood_crown_desc");
		sprite = "item-blood_crown";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		Player.mobSpawnModifier += 1f;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		Player.mobSpawnModifier -= 1f;
	}
}