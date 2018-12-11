package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
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
		scaleMod = 0.5f;

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

	private int setNum = 0;

	@Override
	public void onClick() {
		super.onClick();
		this.setCurrent((this.current + (Input.instance.isDown("second_use") ? -1 : 1)) % this.choices.length);
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
		Graphics.batch.setColor(this.r * this.ar, this.g * this.ag, this.b * this.ab, 1);

		Graphics.batch.end();
		Graphics.shadows.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.draw(Graphics.batch, this.def, 2, 16);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.shadows.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		float w = maxW;

		Graphics.batch.draw(texture, this.x - w / 2 + 2, this.y - h / 2, w / 2 + 4, this.h / 2,
			w, this.h * 2, this.scale, this.scale, 0,
			0, 0, (int) (w + 4), this.h * 2, false, true);

		Graphics.batch.end();
		Graphics.shadows.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.draw(Graphics.batch, this.choices[this.current], 2, 16);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.shadows.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		setColor();

		Graphics.batch.draw(texture, this.x + w / 2 + 2 - this.sizes[this.current], this.y - h / 2, -w / 2 + 4, this.h / 2,
			w, this.h * 2, this.scale, this.scale, 0,
			0, 0, (int) (w + 4), this.h * 2, false, true);

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	protected void setColor() {
		Graphics.batch.setColor(0.6f, 0.6f, 0.6f, 1);
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