package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.CollisionHelper;

public class UiChoice extends UiButton {
	private String[] choices;
	private int current;
	private String def;
	public static float maxW;

	public UiChoice(String label, int x, int y) {
		super(label, x, y);

		this.def = this.label;
	}

	private int[] sizes;

	public UiChoice setChoices(String[] choices) {
		this.choices = choices;

		sizes = new int[choices.length];
		int maxLen = 0;
		int startLen = 0;

		for (int i = 0; i < this.choices.length; i++) {
			if (Locale.has(this.choices[i])) {
				this.choices[i] = Locale.get(this.choices[i]);
			}

			Graphics.layout.setText(Graphics.medium, this.choices[i]);
			this.sizes[i] = (int) Graphics.layout.width;

			if (i == 0) {
				startLen = this.sizes[0];
			}

			if (sizes[i] > maxLen) {
				maxLen = sizes[i];
			}
		}

		if (current < 0) {
			current += this.choices.length;
		}

		this.current = 0;
		this.setLabel(this.def + ": " + this.choices[this.current]);
		float w = this.w - startLen + maxLen;
		maxW = Math.max(w, maxW);

		return this;
	}

	@Override
	public void onClick() {
		super.onClick();
		// fixme: not working
		// this.setCurrent((this.current + (Input.instance.isDown("Mouse1") ? -1 : 1)) % this.choices.length);
	}

	@Override
	protected boolean checkHover() {
		return CollisionHelper.check((int) (Input.instance.uiMouse.x + InGameState.settingsX), (int) Input.instance.uiMouse.y,
			(int) (this.x - maxW / 2 * 1.2f),
			(int) (this.y - this.h / 2 + 3),
			(int) (maxW * 1.2f), this.h);
	}

	@Override
	public void render() {
		Graphics.medium.setColor(this.r * this.ar, this.g * this.ag, this.b * this.ab, 1);
		float w = maxW;
		Graphics.medium.draw(Graphics.batch, this.def, this.x - w / 2 + 4, this.y - h / 2 + 16);
		setColor();
		Graphics.medium.draw(Graphics.batch, this.choices[this.current], this.x + w / 2 + 4 - this.sizes[this.current], this.y - h / 2 + 16);
		Graphics.medium.setColor(1, 1, 1, 1);
	}

	protected void setColor() {
		Graphics.medium.setColor(0.6f, 0.6f, 0.6f, 1);
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