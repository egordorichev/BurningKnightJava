package org.rexellentgames.dungeon.ui;

import org.rexellentgames.dungeon.assets.Graphics;

public class ExpFx extends UiEntity {
	public ExpFx(float x, float y) {
		this.x = this.fromWorldX(x);
		this.y = this.fromWorldY(y);
	}

	@Override
	public void render() {
		Graphics.batch.draw(Graphics.effects, this.x, this.y, 3, 3, 112, 0, 3, 3, false, false);
	}
}