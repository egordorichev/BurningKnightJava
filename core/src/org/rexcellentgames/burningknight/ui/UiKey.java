package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.Log;

public class UiKey extends UiButton {
	private String keyId;
	private String secondLabel;
	private int secondW;

	public UiKey(String label, int x, int y) {
		super(label, x, y);
		this.keyId = label;

		setSecond();
		w += 32f;
		UiChoice.maxW = Math.max(w, UiChoice.maxW);
	}

	@Override
	public void onClick() {
		Audio.playSfx("menu/change_parameter");
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
			} else if (this.secondLabel.equals("Mouse0")) {
				this.secondLabel = "LMB";
			} else if (this.secondLabel.equals("Mouse1")) {
				this.secondLabel = "RMB";
			}
		}

		w -= secondW;

		Graphics.layout.setText(Graphics.medium, this.secondLabel);
		this.secondW = (int) Graphics.layout.width;

		w += secondW;
	}

	@Override
	public void setLabel(String label) {
		super.setLabel(label);

	}

	public void set(String id) {
		Input.listener = null;
		Audio.playSfx("menu/select");
		Input.instance.rebind(this.keyId, Input.instance.getBinding(this.keyId), id);
		setSecond();
	}

	@Override
	public void render() {
		scaleMod = 0.5f;

		Graphics.batch.end();
		Graphics.shadows.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.setColor(this.r * this.ar, this.g * this.ag, this.b * this.ab, 1);
		Graphics.medium.draw(Graphics.batch, this.label, 2, 16);

		float v = 0.7f;
		Graphics.medium.setColor(v, v, v, 1);
		Graphics.medium.draw(Graphics.batch, this.secondLabel, UiChoice.maxW - this.secondW + 2, 16);
		Graphics.medium.setColor(1, 1, 1, 1);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.shadows.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.draw(texture, this.x - UiChoice.maxW / 2 + 2, this.y - this.h / 2, UiChoice.maxW / 2 + 4, this.h / 2 + 6,
			UiChoice.maxW + 5, this.h + 16, this.scale, this.scale, (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / UiChoice.maxW * 5 + 1f)),
			0, 0, (int) (UiChoice.maxW + 5), this.h + 16, false, true);

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	protected boolean checkHover() {
		return CollisionHelper.check((int) (Input.instance.uiMouse.x + InGameState.settingsX), (int) Input.instance.uiMouse.y,
			(int) (this.x - UiChoice.maxW / 2),
			(int) (this.y - this.h / 2 + 3),
			(int) (UiChoice.maxW), this.h);
	}
}