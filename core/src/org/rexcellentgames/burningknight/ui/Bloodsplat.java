package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Bloodsplat extends Entity {
	private static ArrayList<Animation.Frame> blood = Animation.make("fx-bloodsplat").getFrames("idle");

	{
		alwaysActive = true;
		alwaysRender = true;
	}

	private float a;
	private float c;
	private TextureRegion texture;
	private float al;
	private float alm;
	private float r;

	@Override
	public void init() {
		super.init();
		this.c = Random.newFloat(0.1f, 0.2f);
		r = Random.newFloat(0.6f, 0.9f);
		texture = blood.get(Random.newInt(blood.size())).frame;
		a = Random.newFloat(360);
		alm = Random.newFloat(0.5f, 0.8f);
	}

	private boolean second;
	private float t;
	private boolean go;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.game.getState().isPaused()) {
			this.go = true;
		}

		if (this.go) {
			this.al -= dt * 10;
			if (this.al <= 0) {
				this.done = true;
			}
		}

		if (this.second) {
			this.t += dt;

			if (this.t >= 0.5f) {
				this.al -= dt * 2;

				if (this.al <= 0) {
					this.done = true;
				}
			}
		} else {
			this.al += (this.alm - this.al) * dt * 30;

			if (this.al >= this.alm - 0.05f) {
				this.al = 1f;
				this.second = true;
			}
		}
	}

	@Override
	public void render() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.setColor(r, this.c, this.c, this.al);
		Graphics.render(texture, this.x, this.y, this.a, texture.getRegionWidth() / 2, texture.getRegionHeight() / 2, false, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}