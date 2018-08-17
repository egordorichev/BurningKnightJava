package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.MathUtils;

public class UiSlider extends UiButton {
	private float min;
	private float max;
	protected float val;
	private float sw;
	private static TextureRegion slider = Graphics.getTexture("ui-slider");

	public UiSlider(String label, int x, int y) {
		super(label, x, y, true);

		this.min = 0;
		this.max = 1;
		this.val = 1;
	}

	public UiSlider setValue(float val) {
		this.val = val;
		
		return this;
	}

	@Override
	public void setLabel(String label) {
		super.setLabel(label);

		this.x += this.w / 2;
		this.sw = slider.getRegionWidth() + 8;
		this.w += this.sw;
	}

	@Override
	public void render() {
		Graphics.batch.setColor(this.rr * this.ar, this.rg * this.ag, this.rb * this.ab, 1);

		Graphics.batch.end();
		Graphics.shadows.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.draw(Graphics.batch, this.label, 2, 16);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.shadows.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.draw(texture, this.x - this.w / 2 + 2, this.y - this.h / 2, this.w / 2 + 4, this.h / 2,
			this.w, this.h, this.scale, this.scale, 0,
			0, 0, this.w + 4, this.h, false, true);

		Graphics.batch.setColor(1, 1, 1, 1);
		float v = MathUtils.map(this.val, this.min, this.max, 0, this.sw - 8);

		Graphics.render(slider, this.x + this.w / 2 - this.sw, this.y, 0, -8, 1.5f, false, false, this.scale, this.scale);

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.hover && (Input.instance.isDown("mouse1") || Input.instance.isDown("mouse0"))) {
			if (CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y,
				(int) (this.x + this.w / 2 - this.sw + 8), (int) this.y - this.h / 2, (int) (this.sw), this.h)) {

				this.val = MathUtils.clamp(this.min, this.max,
					MathUtils.map(Input.instance.uiMouse.x - this.x - this.w / 2 + this.sw - 8, 0, this.sw, this.min, this.max)
				);

				this.onUpdate();
			}
		}
	}

	public void onUpdate() {

	}
}