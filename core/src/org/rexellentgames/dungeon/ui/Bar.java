package org.rexellentgames.dungeon.ui;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Tween;

public class Bar extends UiEntity {
	protected float max;
	protected float val;
	protected float last;
	protected int tx;
	protected int ty;
	public boolean vertical;

	@Override
	public void render() {
		int w = (int) (this.vertical ? this.w : this.w * (this.last / this.max));
		int h = (int) (this.vertical ? this.h * (this.last / this.max) : this.h);
		int ww = (int) (this.vertical ? this.w : this.w * (this.val / this.max));
		int hh = (int) (this.vertical ? this.h * (this.val / this.max) : this.h);

		Graphics.batch.setColor(0.8f, 0.8f, 0.8f, 1);
		Graphics.batch.draw(Graphics.ui, this.x, this.y, this.tx, (int) (this.ty + this.h - hh), w, hh);

		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.batch.draw(Graphics.ui, this.x, this.y, this.tx, (int) (this.ty + this.h - h), ww, h);
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