package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;

public class Wings extends Equippable {
	{
		name = Locale.get("wings");
		description = Locale.get("wings_desc");
		sprite = "item-wings";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
<<<<<<< HEAD
		this.owner.flight += 1;
=======

		if (this.owner instanceof Player) {
			((Player) this.owner).flight += 1;
		}
>>>>>>> d5dbb7b740601360b76d810651a91d167202839d
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.flight -= 1;
	}

<<<<<<< HEAD
	@Override
	public boolean canBeUpgraded() {
		return false;
=======
		if (this.owner instanceof Player) {
			((Player) this.owner).flight += 1;
		}
>>>>>>> d5dbb7b740601360b76d810651a91d167202839d
	}
}