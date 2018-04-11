package org.rexellentgames.dungeon.entity.creature.mob.boss;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;

public class CrazyKing extends Boss {
	private static Animation animations = Animation.make("actor_towel_king");
	private static AnimationData idle = animations.get("idle");

	{
		hpMax = 100;
		mind = Mind.ATTACKER;
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);
		idle.render(this.x, this.y, this.flipped);
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": return new IdleState();
			case "roam": return new RoamState();
		}

		return super.getAi(state);
	}

	public class CKState extends State<CrazyKing> {

	}

	public class IdleState extends CKState {

	}

	public class RoamState extends CKState {

	}
}