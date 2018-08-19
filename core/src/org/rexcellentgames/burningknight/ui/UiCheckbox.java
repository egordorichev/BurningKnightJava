package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.CollisionHelper;

public class UiCheckbox extends UiButton {
	public UiCheckbox(String label, int x, int y) {
		super(label, x, y);
		this.setColor();
	}

	protected boolean on;

	@Override
	public void setLabel(String label) {
		super.setLabel(label);
	}

	public UiCheckbox setOn(boolean on) {
		this.on = on;
		this.setColor();
		return this;
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

		float a = (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / 26 * 20));

		float y = this.y - 6;
		Graphics.render(box, this.x + this.w / 2 + 22 * scale - 13 + 7.5f, y + 7.5f, a, 7.5f, 7.5f, false, false, scale, scale);

		if (this.on) {
			Graphics.render(mark, this.x + this.w / 2 + 22 * scale - 11 + 4f, y + 3 + 6f, a, 4f, 6f, false, false, scale, scale);
		}
	}

	public static TextureRegion box = Graphics.getTexture("ui-checkbox");
	public static TextureRegion mark = Graphics.getTexture("ui-mark");

	@Override
	public void onClick() {
		super.onClick();

		this.on = !this.on;
		this.setColor();
	}

	@Override
	protected boolean checkHover() {
		return CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y,
			(int) (this.x - this.w / 2 * 1.2f),
			(int) (this.y - this.h / 2),
			(int) (this.w * 1.2f + 15), this.h);
	}
}