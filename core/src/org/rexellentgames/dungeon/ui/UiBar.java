package org.rexellentgames.dungeon.ui;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;
import org.rexellentgames.dungeon.util.Tween;

public class UiBar extends UiEntity {
	protected float max;
	protected float val;
	protected float last;
	protected int tx;
	protected int ty;
	public boolean vertical;
	public boolean hovered;

	@Override
	public void update(float dt) {
		this.hovered = (CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, (int) this.x, (int) this.y,
			(int) this.w, (int) this.h));
	}

	@Override
	public void render() {
		int w = (int) Math.ceil(this.vertical ? this.w : this.w * (this.last / this.max));
		int h = (int) Math.ceil(this.vertical ? this.h * (this.last / this.max) : this.h);
		int ww = (int) Math.ceil(this.vertical ? this.w : this.w * (this.val / this.max));
		int hh = (int) Math.ceil(this.vertical ? this.h * (this.val / this.max) : this.h);

		Graphics.batch.setColor(0.8f, 0.8f, 0.8f, 1);
		// todo
		// Graphics.batch.draw(Graphics.ui, this.x, this.y, this.tx, (int) (this.ty + this.h - hh), w, hh);

		Graphics.batch.setColor(1, 1, 1, 1);
		// Graphics.batch.draw(Graphics.ui, this.x, this.y, this.tx, (int) (this.ty + this.h - h), ww, h);
	}

	public void renderInfo() {
		Graphics.medium.draw(Graphics.batch, (int) this.val + " / " + (int) this.max, Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y + 6);
	}

	public void setValue(float v) {
		if (this.vertical) {
			this.val = v;
			this.last = v;
			return;
		}

		if (v != this.val) {
			this.last = this.val;

			Tween.to(new Tween.Task(v, 0.6f) {
				@Override
				public float getValue() {
					return last;
				}

				@Override
				public void setValue(float value) {
					last = value;
				}
			});
		}

		this.val = v;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public void setTexture(int x, int y) {
		this.tx = x;
		this.ty = y;
	}
}