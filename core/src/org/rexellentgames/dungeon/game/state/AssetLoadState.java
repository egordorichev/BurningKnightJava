package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.net.Network;

public class AssetLoadState extends State {
	@Override
	public void update(float dt) {
		super.update(dt);

		if (Graphics.updateLoading()) {
			if (!Network.NONE) {
				Dungeon.game.setState(new LoginState());
			} else {
				Dungeon.game.setState(new MainMenuState());
				//Dungeon.game.setState(new KeyConfigState());
				//LoadState.readDepth();
			//	Dungeon.goToLevel(Dungeon.depth);
			}
		}
	}

	@Override
	public void render() {
		super.render();

		Graphics.print("Loading... " + (int) Math.floor(Graphics.getPercent() * 100) + "%", Graphics.medium, 100);
	}
}