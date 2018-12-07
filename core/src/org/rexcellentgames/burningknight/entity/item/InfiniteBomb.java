package org.rexcellentgames.burningknight.entity.item;

public class InfiniteBomb extends Bomb {
	{
		sprite = "item-bomb_orbital";
		stackable = false;
		useTime = 0.4f;
		name = "infinite_bomb";
		description = "infinite_bomb_desc";
	}

	@Override
	public void use() {
		super.use();
		setCount(count + 1);
	}
}