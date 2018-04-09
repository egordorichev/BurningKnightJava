package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.audio.Sound;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;

public class IronSword extends Sword {
	{
		name = "Iron Sword";
		description = "Really old iron sword, probably one of the knights lost it.";
		sprite = "item (iron sword)";
		damage = 6;
		useTime = 1f;
	}

	private static Sound sfx = Graphics.getSound("sfx/woosh_towelknight.wav");

	@Override
	protected void onHit(Creature creature) {
		super.onHit(creature);

		if (creature instanceof Mob) {
			((Mob) creature).target = null;
			creature.become("idle");
			((Mob) creature).stupid = true;
		}
	}

	@Override
	public Sound getSfx() {
		return sfx;
	}
}