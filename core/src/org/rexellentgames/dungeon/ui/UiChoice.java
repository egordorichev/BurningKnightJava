package org.rexellentgames.dungeon.ui;

import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Log;

public class UiChoice extends UiButton {
	private String[] choices;
	private int current;

	public UiChoice(String label, int x, int y) {
		super(label, x, y);
	}

	public UiChoice setChoices(String[] choices) {
		this.choices = choices;
		return this;
	}

	@Override
	public void onClick() {
		super.onClick();

		this.setCurrent((this.current + (Input.instance.isDown("mouse1") ? -1 : 1)) % this.choices.length);
	}

	public UiChoice setCurrent(int current) {
		if (current < 0) {
			current += this.choices.length;
		}

		this.current = current;
		this.setLabel(this.choices[this.current]);
		return this;
	}

	public int getCurrent() {
		return this.current;
	}
}