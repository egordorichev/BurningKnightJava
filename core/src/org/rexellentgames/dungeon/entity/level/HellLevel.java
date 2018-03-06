package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LoopBuilder;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;

public class HellLevel extends BetterLevel {
	@Override
	protected Builder getBuilder() {
		LoopBuilder builder = new LoopBuilder();

		builder.setShape(2,
			Random.newFloat(0.4f, 0.7f),
			Random.newFloat(0f, 0.5f));

		return builder;
	}

	@Override
	protected Painter getPainter() {
		return null;
	}
}