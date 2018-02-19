package org.rexellentgames.dungeon.game;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.util.Log;

public class InGameState extends State {
	private Area area;

	@Override
	public void init() {
		this.area = new Area(this);

		Level level = (Level) this.area.add(new RegularLevel());

		if (!level.generate()) {
			Log.error("Failed to generate the level!");
		}
	}

	@Override
	public void update(float dt) {
		this.area.update(dt);
	}

	@Override
	public void render() {
		this.area.render();
	}
}