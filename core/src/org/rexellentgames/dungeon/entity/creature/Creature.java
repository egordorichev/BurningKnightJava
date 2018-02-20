package org.rexellentgames.dungeon.entity.creature;

import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.MathUtils;

public class Creature extends SaveableEntity {
	protected int hp;
	protected int hpMax;
	protected boolean dead;

	@Override
	public void init() {
		super.init();

		this.hp = this.hpMax;
	}

	public void modifyHp(int amount) {
		if (this.dead) {
			return;
		}

		this.hp = MathUtils.clamp(0, this.hpMax, this.hp + amount);

		if (this.hp == 0) {
			this.die();
		}
	}

	protected void die() {
		this.dead = true;
	}

	public int getHp() {
		return this.hp;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getHpMax() {
		return this.hpMax;
	}
}