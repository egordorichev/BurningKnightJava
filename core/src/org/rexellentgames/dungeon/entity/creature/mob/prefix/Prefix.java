package org.rexellentgames.dungeon.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;

public class Prefix {
	protected Mob mob;
	public int id;

	public Color getColor() {
		return Color.RED;
	}

	public void apply(Mob mob) {
		this.mob = mob;
	}

	public void onGenerate() {

	}

	public void onDeath() {

	}
}