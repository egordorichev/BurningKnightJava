package org.rexcellentgames.burningknight.entity.item.key;

public class BurningKey extends Key {
	{
		autoPickup = false;
		sprite = "item-burning_key";
	}

	@Override
	public void onPickup() {
		super.onPickup();
	}
}