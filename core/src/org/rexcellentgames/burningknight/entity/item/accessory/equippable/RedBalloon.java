package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class RedBalloon extends Equippable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.flight += 1;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
<<<<<<< HEAD
		this.owner.flight -= 1;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
=======
		this.owner.flight -=1;
>>>>>>> d5dbb7b740601360b76d810651a91d167202839d
	}
}