package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
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

		UiChoice.maxW = Math.max(w, UiChoice.maxW);
	}

	@Override
	public void render() {
		Graphics.batch.end();
		Graphics.shadows.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.setColor(this.r * this.ar, this.g * this.ag, this.b * this.ab, 1);
		Graphics.medium.draw(Graphics.batch, this.label, 2, 16);

		float w = UiChoice.maxW;
		Graphics.medium.draw(Graphics.batch, this.label, this.ox - (w) * 0.5f + 4, this.y - this.h / 2 + 16);
		Graphics.medium.setColor(1, 1, 1, 1);

		w = slider.getRegionWidth() - 4;
		float v = MathUtils.map(this.val, this.min, this.max, 0, w / 4);

		float vl = 0.2f;
		Graphics.batch.setColor(vl, vl, vl, 1);
		Graphics.render(fill, UiChoice.maxW - w + 2, h * 0.5f + 2, 0, 0, 0, false, false, w / 4 * scale, scale * 1.1f);
		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.render(fill, UiChoice.maxW - w + 2, h * 0.5f + 2, 0, 0, 0, false, false, v * scale, scale * 1.1f);
		Graphics.render(slider, UiChoice.maxW - w, h * 0.5f, 0, 0, 0, false, false, scale, scale);

		String s = ((int)Math.floor((this.val) * 100f)) + "%";
		Graphics.layout.setText(Graphics.small, s);
		Graphics.print(s, Graphics.small, UiChoice.maxW - w * 0.5f - Graphics.layout.width / 2, h * 0.5f + 2);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.shadows.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.batch.draw(texture, this.x - UiChoice.maxW / 2 + 2, this.y - this.h / 2, this.w / 2, this.h / 2 + 8,
			this.w + 72, this.h + 16, this.scale, this.scale, (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / UiChoice.maxW * 10 + 1f)),
			0, 0, this.w + 72, this.h + 16, false, true);

	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if ((this.isSelected || this.hover)) {
			if (Input.instance.wasPressed("ui_left")) {
				this.val = MathUtils.clamp(this.min, this.max, this.val - (this.max - this.min) / 16);
				Audio.playSfx("menu/moving");
				this.onUpdate();
			} else if (Input.instance.wasPressed("ui_right")) {
				this.val = MathUtils.clamp(this.min, this.max, this.val + (this.max - this.min) / 16);
				Audio.playSfx("menu/moving");
				this.onUpdate();
			}

			if (this.checkHover()) {
				if ((Input.instance.isDown("mouse"))) {
					float prev = this.val;

					this.val = MathUtils.clamp(this.min, this.max,
						MathUtils.map(Input.instance.uiMouse.x+ InGameState.settingsX - (this.ox + UiChoice.maxW * 0.5f - (slider.getRegionWidth())), 0, (this.sw - 12) * scale, this.min, this.max)
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
			(int) (this.ox - (UiChoice.maxW * 0.5f)),
			(int) (this.y - 4+ h * 0.5f),
			(int) (UiChoice.maxW * scale), 10);
	}
}