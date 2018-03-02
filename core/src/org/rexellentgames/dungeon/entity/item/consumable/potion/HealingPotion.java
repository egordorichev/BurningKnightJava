package org.rexellentgames.dungeon.entity.item.consumable.potion;

public class HealingPotion extends Potion {
	{
		name = "Healing Potion";
		description = "Makes your health full again";
	}

	@Override
	public void use() {
		super.use();
		this.owner.modifyHp(this.owner.getHpMax() - this.owner.getHp());
	}
}