package org.rexellentgames.dungeon.entity.item.consumable.potion;

public class HealingPotion extends Potion {
	{
		name = "Healing Potion";
	}

	@Override
	public void use() {
		super.use();

		this.owner.modifyHp(10);
	}
}