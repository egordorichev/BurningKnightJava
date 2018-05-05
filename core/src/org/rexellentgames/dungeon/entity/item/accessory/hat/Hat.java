package org.rexellentgames.dungeon.entity.item.accessory.hat;

import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.accessory.Accessory;

public class Hat extends Accessory {
	protected int defense = 1;
	protected String skin;

	@Override
	public void onEquip() {
		super.onEquip();

		this.owner.modifyDefense(this.defense);

		if (this.owner instanceof Player) {
			((Player) this.owner).setSkin(this.skin);
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		this.owner.modifyDefense(-this.defense);

		if (this.owner instanceof Player) {
			((Player) this.owner).setSkin("");
		}
	}
}