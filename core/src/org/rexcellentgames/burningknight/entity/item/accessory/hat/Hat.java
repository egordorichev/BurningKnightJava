package org.rexcellentgames.burningknight.entity.item.accessory.hat;

import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;

public class Hat extends Accessory {
	protected int defense = 2;
	public String skin;

	{
		useable = true;
	}

	@Override
	public void use() {
		super.use();
		int i;

		for (i = 0; i < Player.instance.getInventory().getSize(); i++) {
			if (Player.instance.getInventory().getSlot(i) == this) {
				break;
			}
		}

		UiInventory ui = Player.instance.ui;

		Item item = ui.getInventory().getSlot(6);
		ui.getInventory().setSlot(6, ui.getInventory().getSlot(i));
		ui.getInventory().setSlot(i, item);

		if (item != null) {
			((Accessory) item).equipped = false;
			((Accessory) item).onUnequip(false);
		}

		Accessory ac = ((Accessory) ui.getInventory().getSlot(6));
		ac.onEquip(false);
		ac.equipped = true;
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		// this.owner.modifyDefense(this.defense + this.level - 1);

		if (this.owner instanceof Player) {
			((Player) this.owner).setHat(this.skin);
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		// this.owner.modifyDefense(-this.defense - this.level + 1);

		if (this.owner instanceof Player) {
			((Player) this.owner).setHat("");
		}
	}

	/*
	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\n[orange]");
		builder.append(this.defense + this.level - 1);
		builder.append(" defense[gray]");

		return builder;
	}*/
}