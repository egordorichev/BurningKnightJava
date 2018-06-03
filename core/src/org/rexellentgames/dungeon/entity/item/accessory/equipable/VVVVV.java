package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class VVVVV extends Equipable {
	{
		name = Locale.get("vvvvv");
		description = Locale.get("vvvvv_desc");
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