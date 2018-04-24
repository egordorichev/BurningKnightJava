package org.rexellentgames.dungeon.entity.item.weapon.sword;

import com.badlogic.gdx.audio.Sound;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;

public class SwordA extends Sword {
	{
		name = Locale.get("sword_a");
		description = Locale.get("sword_a_desc");
		sprite = "item (sword A)";
		damage = 4;
		useTime = 0.4f;
	}

	private static Sound sfx = Graphics.getSound("sword_2");

	@Override
	public Sound getSfx() {
		return sfx;
	}
}