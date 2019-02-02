package org.rexcellentgames.burningknight.entity.creature.mob.boss;

public class BossPattern {
	public int getNumAttacks() {
		return 3;
	}

	public String getState(int pat, int i) {
		if (true) {
			return "nano";
		}

		if (pat == 0) {
			if (i == 0) {
				return get00();
			} else if (i == 1) {
				return get01();
			} else {
				return get02();
			}
		} else if (pat == 1) {
			if (i == 0) {
				return get10();
			} else if (i == 1) {
				return get11();
			} else {
				return get12();
			}
		} else {
			if (i == 0) {
				return get20();
			} else if (i == 1) {
				return get21();
			} else {
				return get22();
			}
		}
	}

	public String get00() {
		return "laserAttack";
	}

	public String get01() {
		return "missileAttack";
	}

	public String get02() {
		return "laserAimAttack";
	}

	public String get10() {
		return "autoAttack";
	}

	public String get11() {
		return "missileAttack";
	}

	public String get12() {
		return "spawnAttack";
	}

	public String get20() {
		return "autoAttack";
	}

	public String get21() {
		return "spawnAttack";
	}

	public String get22() {
		return "laserAimAttack";
	}
}