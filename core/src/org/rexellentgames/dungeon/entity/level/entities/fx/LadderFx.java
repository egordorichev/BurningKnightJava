package org.rexellentgames.dungeon.entity.level.entities.fx;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.levels.WaveLevel;
import org.rexellentgames.dungeon.game.input.Input;

public class LadderFx extends Entity {
	private Entity ladder;
	private String text;

	public LadderFx(Entity ladder, String text) {
		this.ladder = ladder;
		this.text = text;

		GlyphLayout layout = new GlyphLayout(Graphics.medium, this.text);

		this.x = ladder.x + 8 - layout.width / 2;
		this.y = ladder.y + 32;

		this.depth = 10;
	}

	@Override
	public void render() {
		Graphics.medium.draw(Graphics.batch, this.text, this.x, this.y);

		if (Input.instance.wasPressed("action")) {
			this.done = true;

			if (this.ladder instanceof Entrance) {
				if (Dungeon.depth != 0) {
					Dungeon.loadType = Entrance.LoadType.GO_UP;
					Dungeon.ladderId = ((Entrance) this.ladder).getType();

					Dungeon.goToLevel(Dungeon.depth - 1);
				} else {
					UiLog.instance.print("[orange]You cant leave just yet!");
				}
			} else if (this.ladder instanceof Exit) {
				if (Dungeon.depth != 4 || Dungeon.level instanceof WaveLevel) {
					Dungeon.loadType = Entrance.LoadType.GO_DOWN;
					Dungeon.ladderId = ((Exit) this.ladder).getType();

					Dungeon.goToLevel(Dungeon.depth + 1);
				} else {
					UiLog.instance.print("[red]Not implemented just yet!");
				}
			}
		}
	}
}