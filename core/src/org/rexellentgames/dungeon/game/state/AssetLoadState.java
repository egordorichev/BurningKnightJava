package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;

public class AssetLoadState extends State {
	public static final boolean START_TO_MENU = false;
	public static boolean done = false;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Graphics.updateLoading()) {
			done = true;

			if (START_TO_MENU) {
				Dungeon.game.setState(new MainMenuState());
			} else {
				Dungeon.goToLevel(Dungeon.depth);
			}
		}
	}

	@Override
	public void render() {
		super.render();

		Graphics.print("Loading... " + (int) Math.floor(Graphics.getPercent() * 100) + "%", Graphics.medium, 100);
	}
}