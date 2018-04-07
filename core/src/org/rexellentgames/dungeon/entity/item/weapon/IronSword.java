package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.audio.Sound;
import org.rexellentgames.dungeon.assets.Graphics;

public class IronSword extends Sword {
	{
		name = "Iron Sword";
		description = "Really old iron sword, probably one of the knights lost it.";
		sprite = "item (iron sword)";
		damage = 11;
		useTime = 1f;
	}

	private static Sound sfx = Graphics.getSound("sfx/woosh_towelknight.wav");

	@Override
	public Sound getSfx() {
		return sfx;
	}
}