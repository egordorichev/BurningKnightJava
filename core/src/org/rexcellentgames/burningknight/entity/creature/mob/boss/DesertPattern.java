package org.rexcellentgames.burningknight.entity.creature.mob.boss;

public class DesertPattern extends BossPattern {
	@Override
	public int getNumAttacks() {
		return 2;
	}

	@Override
	public String get00() {
		return "chase";
	}

	@Override
	public String get01() {
		return "tpntack";
	}

	@Override
	public String get02() {
		return "spawnAttack";
	}

	@Override
	public String get10() {
		return "chase";
	}

	@Override
	public String get11() {
		return "tpntack";
	}

	@Override
	public String get12() {
		return "spin";
	}
}