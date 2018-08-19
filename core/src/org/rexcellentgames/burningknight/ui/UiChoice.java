package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.game.input.Input;

public class UiChoice extends UiButton {
	private String[] choices;
	private int current;
	private String def;

	public UiChoice(String label, int x, int y) {
		super(label, x, y);

		this.def = this.label;
	}

	public UiChoice setChoices(String[] choices) {
		this.choices = choices;

		for (int i = 0; i < this.choices.length; i++) {
			if (Locale.has(this.choices[i])) {
				this.choices[i] = Locale.get(this.choices[i]);
			}
		}

		this.setCurrent(0);

		return this;
	}

	@Override
	public void onClick() {
		super.onClick();
		this.setCurrent((this.current + (Input.instance.isDown("second_use") ? -1 : 1)) % this.choices.length);
	}

	public UiChoice setCurrent(int current) {
		if (current < 0) {
			current += this.choices.length;
		}

		this.current = current;
		this.setLabel(this.def + ": " + this.choices[this.current]);
		this.onUpdate();

		return this;
	}

	public int getCurrent() {
		return this.current;
	}

	public void onUpdate() {

	}
}