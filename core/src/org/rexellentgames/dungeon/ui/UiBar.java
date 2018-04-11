package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;
import org.rexellentgames.dungeon.util.Tween;

public class UiBar extends UiEntity {
	protected float max;
	protected float val;
	protected float last;
	public boolean vertical;
	public boolean hovered;
	public TextureRegion region;

	@Override
	public void update(float dt) {
		this.hovered = (CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, (int) this.x, (int) this.y,
			(int) this.w, (int) this.h));
	}

	@Override
	public void render() {
		if (this.vertical) {
			this.render2();
			this.render1();
		} else {
			this.render1();
			this.render2();
		}
	}

	protected void render1() {
		int w = (int) Math.ceil(this.vertical ? this.w : this.w * (this.last / this.max));
		int h = (int) Math.ceil(this.vertical ? this.h * (this.last / this.max) : this.h);

		Graphics.batch.setColor(0.1f, 0.1f, 0.1f, 1);
		Graphics.batch.draw(this.region, this.x, this.y + (this.vertical ? 0 : 1));

		TextureRegion region = new TextureRegion(this.region);

		region.setRegionX(region.getRegionX() + (int) (this.w - w));
		region.setRegionY(region.getRegionY() + (int) (this.h - h));
		region.setRegionWidth(w);
		region.setRegionHeight(h);
	}

	protected void render2() {
		TextureRegion region = new TextureRegion(this.region);

		int ww = (int) Math.ceil(this.vertical ? this.w : this.w * (this.val / this.max));
		int hh = (int) Math.ceil(this.vertical ? this.h * (this.val / this.max) : this.h);
		Graphics.batch.setColor(0.5f, 0.5f, 0.5f, 1);
		Graphics.batch.draw(region, this.x, this.y);

		region.setRegion(this.region);

		region.setRegionX(region.getRegionX() + (int) (this.w - ww));
		region.setRegionY(region.getRegionY() + (int) (this.h - hh));
		region.setRegionWidth(ww);
		region.setRegionHeight(hh);

		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.batch.draw(region, this.x, this.y);
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
}