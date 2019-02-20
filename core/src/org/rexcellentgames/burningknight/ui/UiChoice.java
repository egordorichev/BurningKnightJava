package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.rexcellentgames.burningknight.Dungeon;
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

	@Override
	public void onClick() {
		super.onClick();
		// fixme: not working with lmb
		this.setCurrent((this.current + (Input.instance.isDown("Mouse1") ? -1 : 1)) % this.choices.length);
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
		Graphics.batch.end();
		Graphics.shadows.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.setColor(this.r * this.ar, this.g * this.ag, this.b * this.ab, 1);
		Graphics.medium.draw(Graphics.batch, this.def, 2, 16);
		setColor();
		Graphics.medium.draw(Graphics.batch, this.choices[this.current], maxW + 4 - this.sizes[this.current], 16);
		Graphics.medium.setColor(1, 1, 1, 1);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.shadows.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.draw(texture, this.x - maxW / 2 + 2, this.y - this.h / 2, maxW / 2 + 4, this.h / 2 + 6,
			maxW + 5, this.h + 16, this.scale, this.scale, (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / maxW * 16 + 1f)),
			0, 0, (int) (maxW + 5), this.h + 16, false, true);

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	protected void setColor() {
		Graphics.medium.setColor(0.6f, 0.6f, 0.6f, 1);
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