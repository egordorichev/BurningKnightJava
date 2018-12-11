package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.MathUtils;

public class UiSlider extends UiButton {
	private float min;
	private float max;
	protected float val;
	private float sw;
	private static TextureRegion slider = Graphics.getTexture("ui-slider");
	private static TextureRegion fill = Graphics.getTexture("ui-slider_fill");
	private float ox;

	public UiSlider(String label, int x, int y) {
		super(label, x, y, true);

		this.ox = x;
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

		Graphics.batch.draw(texture, this.ox - 4 - (this.w - this.sw), this.y - this.h / 2, this.w / 2 + 4, this.h / 2,
			this.w, this.h * 2, scale, scale, 0,
			0, 0, this.w + 4, this.h * 2, false, true);

		Graphics.batch.setColor(1, 1, 1, 1);
		float w = slider.getRegionWidth() - 4;
		float v = MathUtils.map(this.val, this.min, this.max, 0, w / 4);



		Graphics.render(fill, this.ox + 6, this.y - 2 + h * 0.5f, 0, 0, 0, false, false, v * scale, scale * 1.1f);
		Graphics.render(slider, this.ox + 4, this.y - 4 + h * 0.5f, 0, 0, 0, false, false, scale, scale);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if ((this.isSelected || this.hover)) {
			if (Input.instance.wasPressed("left")) {
				this.val = MathUtils.clamp(this.min, this.max, this.val - (this.max - this.min) / 16);
				Audio.playSfx("menu/moving");
				this.onUpdate();
			} else if (Input.instance.wasPressed("right")) {
				this.val = MathUtils.clamp(this.min, this.max, this.val + (this.max - this.min) / 16);
				Audio.playSfx("menu/moving");
				this.onUpdate();
			}

			if (this.checkHover()) {
				if ((Input.instance.isDown("use"))) {
					float prev = this.val;

					this.val = MathUtils.clamp(this.min, this.max,
						MathUtils.map(Input.instance.uiMouse.x+ InGameState.settingsX - (this.ox + 6) + 4, 0, (this.sw - 12) * scale, this.min, this.max)
					);

					this.val = (float) (Math.floor(this.val * 16) / 16);

					if (Float.compare(prev, this.val) != 0) {
						Audio.playSfx("menu/moving");
						this.onUpdate();
					}
				} else if (Input.instance.wasPressed("scroll")) {
					float last = this.val;
					this.val = MathUtils.clamp(this.min, this.max, this.val + 1f / 16f * Input.instance.getAmount());

					if (Float.compare(last, this.val) != 0) {
						Audio.playSfx("menu/moving");
						onUpdate();
					}
				}
			}
		}
	}

	public void onUpdate() {

	}

	@Override
	protected boolean checkHover() {
		return CollisionHelper.check((int) (Input.instance.uiMouse.x + InGameState.settingsX), (int) Input.instance.uiMouse.y,
			(int) (this.ox - (this.w - this.sw) - 2),
			(int) (this.y - 4+ h * 0.5f),
			(int) (this.w * scale), 10);
	}
}