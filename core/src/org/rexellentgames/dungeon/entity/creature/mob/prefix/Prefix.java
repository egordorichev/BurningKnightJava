package org.rexellentgames.dungeon.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;

public class Prefix {
	public int id;

	public Color getColor() {
		return Color.RED;
	}

	public void apply(Mob mob) {

	}

	public void onGenerate(Mob mob) {

	}

	public void onDeath(Mob mob) {

	}
}