package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.assets.Graphics;

public class UiCheckbox extends UiChoice {
	public UiCheckbox(String label, int x, int y) {
		super(label, x, y);

		this.setChoices(new String[] {
			"on", "off"
		});
	}

	public UiCheckbox setOn(boolean on) {
		this.setCurrent(on ? 0 : 1);
		return this;
	}

	public boolean isOn() {
		return getCurrent() == 0;
	}

	@Override
	protected void setColor() {
		if (isOn()) {
			Graphics.medium.setColor(0.2f, 0.8f, 0.2f, 1f);
		} else {
			Graphics.medium.setColor(0.8f, 0.2f, 0.2f, 1f);
		}
	}
}