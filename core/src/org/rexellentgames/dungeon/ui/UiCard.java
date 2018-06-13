package org.rexellentgames.dungeon.ui;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;

public class UiCard extends UiButton {
	public UiCard(int x, int y) {
		super(null, x, y);

		this.w = 64;
		this.h = 96;
	}

	@Override
	public void render() {
		Graphics.startShape();
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.setColor(1, 1, 1, 1);

		float w = this.w * scale;
		float h = this.h * scale;

		Graphics.shape.rect(this.x - w / 2, this.y - h / 2 + this.h / 2, w, h);
		Graphics.endShape();
	}
}