package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class VVVVV extends Equipable {
	{
		name = Locale.get("vvvvv");
		description = Locale.get("vvvvv_desc");
		sprite = "item-vvvvv";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		Dungeon.flip = !Dungeon.flip;

		if (this.owner instanceof Player) {
			((Player) this.owner).goldModifier += 30f;
		}
	}

	@Override
	public void onUnequip() {
		super.onEquip();

		Dungeon.flip = !Dungeon.flip;

		if (this.owner instanceof Player) {
			((Player) this.owner).goldModifier -= 30f;
		}
	}
}