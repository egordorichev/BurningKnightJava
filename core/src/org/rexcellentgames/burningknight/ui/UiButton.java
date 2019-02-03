package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.CollisionHelper;

public class UiButton extends UiEntity {
	public int h;
	public int w;

	protected boolean hover;
	public float scale = 1f;
	protected String label;
	protected float mx = 0f;
	public boolean sparks;
	public float r = 1f;
	public float g = 1f;
	public float b = 1f;
	public float rr = 1f;
	public float rg = 1f;
	public float rb = 1f;
	protected float ar = 1f;
	protected float ag = 1f;
	protected float ab = 1f;
	private boolean disableClick;

	public UiButton(String label, int x, int y) {
		this.setLabel(label);

		this.y = y;
		this.x = x;
	}

	public UiButton(String label, int x, int y, boolean disableClick) {
		this.setLabel(label);

		this.y = y;
		this.x = x;
		this.disableClick = disableClick;
	}

	public UiButton setSparks(boolean sparks) {
		this.sparks = sparks;
		return this;
	}
	
	public void setLabel(String label) {
		if (label == null) {
			return;
		}

		String old = label;

		if (Locale.has(label)) {
			label = Locale.get(label);
		}

		this.label = label;
		this.modLabel(old);

		Graphics.layout.setText(Graphics.medium, this.label);

		this.w = (int) Graphics.layout.width;
		this.h = (int) Graphics.layout.height;
	}

	protected void modLabel(String old) {

	}

	@Override
	public void render() {
		Graphics.medium.setColor(this.r * this.ar, this.g * this.ag, this.b * this.ab, 1);
		Graphics.medium.draw(Graphics.batch, this.label, this.x - this.w / 2 + 2, this.y - h / 2 + 16);
		Graphics.medium.setColor(1, 1, 1, 1);
	}

	protected boolean playSfx = true;

	public UiButton setPlaySfx(boolean playSfx) {
		this.playSfx = playSfx;
		return this;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.wasPressed("mouse") && !disableClick && checkHover()) {
			this.onClick();
			this.rr = 0.3f;
			this.rg = 0.3f;
			this.rb = 0.3f;

			r = rr;
			g = rg;
			b = rb;
		}

		if (this.sparks) {
			// still wrong
			// Spark.random(this.x - this.w / 2, this.y - this.h / 4, this.w, this.h / 2, true);
		}

		this.rr += (this.r - this.rr) * dt * 2;
		this.rg += (this.g - this.rg) * dt * 2;
		this.rb += (this.b - this.rb) * dt * 2;

		boolean h = this.hover;
		this.hover = checkHover() || isSelected;

		if (h && !this.hover) {
			onUnhover();
		} else if ((!h && this.hover)) {
			this.r = 1f;
			this.g = 1f;
			this.b = 1f;

			Audio.playSfx("menu/moving");
			onHover();
		}

		if (this.hover) {
			float v = 0.5f;

			this.r = v;
			this.g = v;
			this.b = v;
		} else {
			r = 1f;
			g = 1f;
			b = 1f;
		}
	}

	protected void onUnhover() {

	}

	protected void onHover() {

	}

	public void onClick() {
		if (this.playSfx) {
			Audio.playSfx("menu/select");
		}
	}

	protected boolean checkHover() {
		return CollisionHelper.check((int) (Input.instance.uiMouse.x + InGameState.settingsX), (int) Input.instance.uiMouse.y,
			(int) (this.x - this.w / 2),
			(int) (this.y - this.h / 2 + 3),
			(int) (this.w), this.h);
	}
}