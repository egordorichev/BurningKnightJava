package org.rexcellentgames.burningknight.entity.creature.mob.boss;

public class ForestBossPattern extends BossPattern {
	@Override
	public int getNumAttacks() {
		return 3;
	}

	@Override
	public String get00() {
		return "tear";
	}

	@Override
	public String get01() {
		return "tpntack";
	}

	@Override
	public String get02() {
		return "circ";
	}

	@Override
	public String get10() {
		return "circ";
	}

	@Override
	public String get11() {
		return "laserAimAttack";
	}

	@Override
	public String get12() {
		return "spin";
	}

	@Override
	public String get20() {
		return "tear";
	}

	@Override
	public String get21() {
		return "laserAimAttack";
	}

	@Override
	public String get22() {
		return "circ";
	}
}