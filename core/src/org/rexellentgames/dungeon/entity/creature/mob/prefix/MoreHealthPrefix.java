package org.rexellentgames.dungeon.entity.creature.mob.prefix;

public class MoreHealthPrefix extends Prefix {
	@Override
	public void onGenerate() {
		super.onGenerate();

		int add = (int) Math.ceil(((float) this.mob.getHpMax()) / 3f);

		this.mob.setHpMax(this.mob.getHpMax() + add);
		this.mob.modifyHp(add, null);
	}
}