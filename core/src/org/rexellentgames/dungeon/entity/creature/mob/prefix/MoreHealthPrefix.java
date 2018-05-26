package org.rexellentgames.dungeon.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;

public class MoreHealthPrefix extends Prefix {
	private static Color color = Color.valueOf("#ac3232");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void onGenerate() {
		super.onGenerate();

		int add = (int) Math.ceil(((float) this.mob.getHpMax()) / 3f);

		this.mob.setHpMax(this.mob.getHpMax() + add);
		this.mob.modifyHp(add, null);
	}
}