package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Log;

public class UiKey extends UiButton {
	private String keyId;
	private String secondLabel;
	private int secondW;

	public UiKey(String label, int x, int y) {
		super(label, x, y);
		this.keyId = label;

		setSecond();
	}

	@Override
	public void onClick() {
		Input.listener = this;
		setSecond();
	}

	protected void setSecond() {
		if (Input.listener == this) {
			this.secondLabel = Locale.get("assign_key");
		} else {
			this.secondLabel = Input.instance.getBinding(this.keyId);

			if (this.secondLabel == null) {
				this.secondLabel = Locale.get("none");
			}
		}

		Graphics.layout.setText(Graphics.medium, this.secondLabel);
		this.secondW = (int) Graphics.layout.width;
	}

	@Override
	public void setLabel(String label) {
		super.setLabel(label);

		this.w = 186;
	}

	public void set(String id) {
		Input.listener = null;

		Log.info(Input.instance.getBinding(this.keyId) + " to " + id);

		Input.instance.rebind(this.keyId, Input.instance.getBinding(this.keyId), id);

		setSecond();

		Log.info(this.secondLabel);
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

		Graphics.batch.draw(texture, this.x - this.w / 2 + 2, this.y - this.h / 2,
			this.w / 2 + 4, this.h / 2,

			this.w, this.h, this.scale, this.scale, (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / this.w * 20)),
			0, 0, this.w + 4, this.h, false, true);

		// Second part

		Graphics.batch.setColor(this.rr * this.ar, this.rg * this.ag, this.rb * this.ab, 1);

		Graphics.batch.end();
		Graphics.shadows.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.draw(Graphics.batch, this.secondLabel, 2, 16);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.shadows.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		texture = Graphics.text.getColorBufferTexture();

		Graphics.batch.draw(texture, this.x + this.w / 2 - this.secondW - 2, this.y - this.h / 2,
			-this.w / 2 + 4, this.h / 2,
			this.secondW, this.h, this.scale, this.scale, (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / this.w  * 20)),
			0, 0, this.secondW + 4, this.h, false, true);

		Graphics.batch.setColor(1, 1, 1, 1);
	}
}