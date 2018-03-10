package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.assets.Graphics;

public class NetLoadState extends State {
	@Override
	public void init() {

	}

	@Override
	public void render() {
		Graphics.medium.draw(Graphics.batch, "Loading...", 10, 20);
	}
}