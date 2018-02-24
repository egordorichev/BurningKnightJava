package org.rexellentgames.dungeon.entity.creature.buff;

public class Buff {
	protected String name;
	protected String description;
	protected int sprite;
	protected float duration;
	protected float time;
	protected boolean bad = false;

	public Buff(float duration) {
		this.duration = duration;
	}

	public Buff() {

	}
}