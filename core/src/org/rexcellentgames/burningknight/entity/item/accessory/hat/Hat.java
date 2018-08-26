package org.rexcellentgames.burningknight.entity.item.accessory.hat;

import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;

public class Hat extends Accessory {
	protected int defense = 2;
	protected String skin;

	{
		useable = true;
	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}

	@Override
	public void use() {
		super.use();

		UiInventory ui = Player.instance.ui;

		Item item = ui.getInventory().getSlot(6);
		ui.getInventory().setSlot(6, ui.getInventory().getSlot(ui.getActive()));
		ui.getInventory().setSlot(ui.getActive(), item);

		if (item != null) {
			((Accessory) item).onUnequip(false);
		}

		((Accessory) ui.getInventory().getSlot(6)).onEquip(false);
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		this.owner.modifyDefense(this.defense + this.level - 1);

		if (this.owner instanceof Player) {
			((Player) this.owner).setHat(this.skin);
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		this.owner.modifyDefense(-this.defense - this.level + 1);

		if (this.owner instanceof Player) {
			((Player) this.owner).setHat("");
		}
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\n[orange]");
		builder.append(this.defense + this.level - 1);
		builder.append(" defense[gray]");

		return builder;
	}
}