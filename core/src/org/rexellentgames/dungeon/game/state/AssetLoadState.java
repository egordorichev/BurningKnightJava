package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.net.Network;

public class AssetLoadState extends State {
	public static final boolean START_TO_MENU = true;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Graphics.updateLoading()) {
			if (!Network.NONE) {
				Dungeon.game.setState(new LoginState());
			} else {
				if (START_TO_MENU) {
					Dungeon.game.setState(new MainMenuState());
				} else {
					LoadState.readDepth();
					Dungeon.goToLevel(Dungeon.depth);
				}
			}
		}
	}

	@Override
	public void render() {
		super.render();

		Graphics.print("Loading... " + (int) Math.floor(Graphics.getPercent() * 100) + "%", Graphics.medium, 100);
	}
}