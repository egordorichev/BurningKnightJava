package org.rexcellentgames.burningknight.entity.creature.mob.boss;

public class LibraryPattern extends BossPattern {
	@Override
	public int getNumAttacks() {
		return 3;
	}

	@Override
	public String get00() {
		return "spawnAttack";
	}

	@Override
	public String get01() {
		return "cshoot";
	}

	@Override
	public String get02() {
		return "weird";
	}

	@Override
	public String get10() {
		return "book";
	}

	@Override
	public String get11() {
		return "line";
	}

	@Override
	public String get12() {
		return "weird";
	}

	@Override
	public String get20() {
		return "book";
	}

	@Override
	public String get21() {
		return "line";
	}

	@Override
	public String get22() {
		return "cshoot";
	}
}