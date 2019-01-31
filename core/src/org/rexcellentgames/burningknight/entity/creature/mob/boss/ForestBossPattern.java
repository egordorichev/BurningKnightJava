package org.rexcellentgames.burningknight.entity.creature.mob.boss;

public class ForestBossPattern extends BossPattern {
	@Override
	public int getNumAttacks() {
		return 1;
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
		return "nano";
	}

	@Override
	public String get11() {
		return "tpntack";
	}

	@Override
	public String get12() {
		return "spin";
	}

	@Override
	public String get20() {
		return "spin";
	}

	@Override
	public String get21() {
		return "nano";
	}

	@Override
	public String get22() {
		return "spawnAttack";
	}
}