package org.rexellentgames.dungeon.entity.plant;

import org.rexellentgames.dungeon.util.Animation;

public class Lightroom extends Plant {
	private static Animation animations = Animation.make("veggie", "-shroom");

	{
		animation = animations.get("growth");
	}
}