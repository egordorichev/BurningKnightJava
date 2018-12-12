package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class BloodSplatFx extends Entity {
	private float al;
	private PolygonSprite poly;
	public static PolygonSpriteBatch polyBatch;
	private static Texture textureSolid;
	private static EarClippingTriangulator triangulator = new EarClippingTriangulator();
	public float sizeMod = 1;
	public boolean lng = false;
	public boolean cntr = false;
	public float a = -1;

	{
		depth = -9;
	}

	@Override
	public void init() {
		super.init();

		if (polyBatch == null) {
			polyBatch = new PolygonSpriteBatch();

			Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			pix.setColor(0xFFFFFFFF);
			pix.fill();
			textureSolid = new Texture(pix);
		}

		sizeMod *= 0.5f;
		int count = (int) (Random.newInt(6, 15) * sizeMod);
		float[] shape = new float[count * 2];
		float am = 4;
		float d = Random.newFloat(8, 13) * sizeMod;
		float w = Random.newFloat(cntr ? 32 : 16, cntr ? 64 : 24);

		for (int i = 0; i < count; i++) {
			float a = (float) (((float) i) / count * Math.PI * 2);

			if (lng) {
				shape[i * 2] = (float) (Math.cos(a) * w) + Random.newFloat(-am, am) + (cntr ? 0 : w);
				shape[i * 2 + 1] = (float) (Math.sin(a) * 4) + Random.newFloat(-am, am);
			} else {
				shape[i * 2] = (float) (Math.cos(a) * d) + Random.newFloat(-am, am);
				shape[i * 2 + 1] = (float) (Math.sin(a) * d) + Random.newFloat(-am, am);
			}
		}

		poly = new PolygonSprite(new PolygonRegion(new TextureRegion(textureSolid), shape, triangulator.computeTriangles(shape).toArray()));

		if (a == -1) {
			a = Random.newFloat(360);
		}

		poly.setRotation(a);
		poly.setPosition(x + 8, y + 8);

		this.w = 64;
		this.h = 64;
	}

	@Override
	public boolean isOnScreen() {
		OrthographicCamera camera = Camera.game;

		float zoom = camera.zoom;

		return this.x + this.w * 1.5f >= camera.position.x - Display.GAME_WIDTH / 2 * zoom &&
			this.y + this.h * 1.5f >= camera.position.y - Display.GAME_HEIGHT / 2 * zoom &&
			this.x - this.w <= camera.position.x + Display.GAME_WIDTH / 2 * zoom &&
			this.y - this.h <= camera.position.y + this.h + Display.GAME_HEIGHT / 2 * zoom;
	}

	private float t;
	private float alp = 1;

	@Override
	public void update(float dt) {
		super.update(dt);

		t += dt;

		if (Settings.quality == 0 && this.t >= 5f) {
			this.alp -= dt * 0.3f;

			if (this.alp <= 0) {
				this.done = true;
			}
		}

		if (this.al < 1f) {
			this.al = Math.min(1f, this.al + dt * 10f);
		}
	}

	@Override
	public void render() {
		Graphics.batch.end();

		poly.setColor(0.9f, 0, 0, 1);
		polyBatch.setProjectionMatrix(Camera.game.combined);
		polyBatch.begin();
		polyBatch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);

		if (lng) {
			poly.setScale(al * this.alp, this.alp);
		} else {
			poly.setScale(al * this.alp);
		}

		poly.draw(polyBatch);
		polyBatch.end();

		polyBatch.setBlendFunction(Graphics.batch.getBlendSrcFunc(), Graphics.batch.getBlendDstFunc());
		Graphics.batch.begin();
	}
}