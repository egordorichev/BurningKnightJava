package org.rexcellentgames.burningknight.entity.creature.mob.tech;

import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

import java.util.ArrayList;

public class Bot extends Mob {
	public static ArrayList<DeathData> data = new ArrayList<>();

	@Override
	protected void deathEffects() {
		super.deathEffects();

		DeathData data = new DeathData();

		data.type = this.getClass();
		data.x = (float) (Math.floor(this.x / 16) * 16 + 8);
		data.y = (float) (Math.floor(this.y / 16) * 16 + 8);

		Bot.data.add(data);
	}

	public class DeathData {
		public Class type;
		public float x;
		public float y;
	}
}