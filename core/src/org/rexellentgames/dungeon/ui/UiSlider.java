package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Spark;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;
import org.rexellentgames.dungeon.util.MathUtils;

public class UiSlider extends UiButton {
	private float min;
	private float max;
	protected float val;
	private static TextureRegion slider = Graphics.getTexture("menu_label (slider)");
	private static TextureRegion handle = Graphics.getTexture("menu_label (slider_handle)");

	public UiSlider setValue(float val) {
		this.val = val;
		return this;
	}

	public UiSlider(String label, int x, int y) {
		super(label, x, y);

		this.min = 0;
		this.max = 1;
		this.val = 1;
	}

	@Override
	public void setLabel(String label) {
		super.setLabel(label);

		this.x += this.w / 2;
		this.w += slider.getRegionWidth() + 8;
	}

	@Override
	public void render() {
		if (this.sparks) {
			Spark.random(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h);
		}

		float a = (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (3f / this.w * 20));

		Graphics.batch.setColor(this.rr * this.ar, this.rg * this.ag, this.rb * this.ab, 1);
		Graphics.render(this.region, this.x, this.y, a,
			this.w - slider.getRegionWidth() - 8, this.region.getRegionHeight() / 2, false, false, this.scale, this.scale);

		Graphics.render(slider, this.x, this.y - 2,
			a, -8, slider.getRegionHeight() / 2, false, false, this.scale, this.scale);

		float v = MathUtils.map(this.val, this.min, this.max, 0, slider.getRegionWidth());

		Graphics.render(handle, this.x, this.y - 2,
			a, -8 - v, handle.getRegionHeight() / 2, false, false, this.scale, this.scale);

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.hover && (Input.instance.isDown("mouse1") || Input.instance.isDown("mouse0"))) {
			if (CollisionHelper.check((int) Input.instance.worldMouse.x, (int) Input.instance.worldMouse.y, (int) this.x, (int) this.y - this.h / 2, this.w / 2 + 4, this.h)) {
				this.val = MathUtils.clamp(this.min, this.max, MathUtils.map(Input.instance.worldMouse.x - this.x, 0, this.w / 2 + 4, this.min, this.max));
				this.onUpdate();
			}
		}
	}

	public void onUpdate() {

	}
}