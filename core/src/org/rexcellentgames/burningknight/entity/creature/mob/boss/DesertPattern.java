package org.rexcellentgames.burningknight.entity.creature.mob.boss;

public class DesertPattern extends BossPattern {
	@Override
	public int getNumAttacks() {
		return 1;
	}

	@Override
	public String get00() {
		return "tpntack";
	}

	@Override
	public String get01() {
		return "tpntack";
	}

	@Override
	public String get02() {
		return "tpntack";
	}

	@Override
	public String getState(int pat, int i) {
		return "tpntack";
	}
}