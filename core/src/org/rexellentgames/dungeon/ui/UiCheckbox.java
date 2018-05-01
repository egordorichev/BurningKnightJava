package org.rexellentgames.dungeon.ui;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.ui.UiButton;

public class UiCheckbox extends UiButton {
	public UiCheckbox(String label, int x, int y) {
		super(label, x, y);
		this.setColor();
	}

	protected boolean on;

	public void setOn(boolean on) {
		this.on = on;
		this.setColor();
	}

	private void setColor() {
		if (!this.on) {
			this.ar = 1f;
			this.ag = 0.3f;
			this.ab = 0.3f;
		} else {
			this.ar = 0.3f;
			this.ag = 1f;
			this.ab = 0.3f;
		}
	}

	public boolean isOn() {
		return on;
	}

	@Override
	public void render() {
		this.setColor();
		super.render();
	}

	@Override
	public void onClick() {
		super.onClick();

		this.on = !this.on;
		this.setColor();
	}
}