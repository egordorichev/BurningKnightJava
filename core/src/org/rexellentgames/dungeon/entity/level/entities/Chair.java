package org.rexellentgames.dungeon.entity.level.entities;

public class Chair extends Prop {
	public boolean flipped;

	@Override
	public void init() {
		this.sprite = flipped ? "biome-0 (chair A)" : "biome-0 (chair B)";
		super.init();
	}
}