package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class BloodSplatFx extends Entity {
	private float al;
	private PolygonSprite poly;
	private static PolygonSpriteBatch polyBatch;
	private static Texture textureSolid;
	private static EarClippingTriangulator triangulator = new EarClippingTriangulator();

	{
		depth = -9;
	}

	@Override
	public void init() {
		super.init();

		if (polyBatch == null) {
			polyBatch = new PolygonSpriteBatch();

			Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			pix.setColor(0xFFFFFFFF); // DE is red, AD is green and BE is blue.
			pix.fill();
			textureSolid = new Texture(pix);
		}

		float an = Random.newFloat(360);
		int count = Random.newInt(10, 20);
		float[] shape = new float[count * 2];
		float am = 4;
		float d = Random.newFloat(6, 10);

		for (int i = 0; i < count; i++) {
			float a = (float) (((float) i) / count * Math.PI * 2) + an;

			shape[i * 2] = (float) (Math.cos(a) * d) + Random.newFloat(-am, am);
			shape[i * 2 + 1] = (float) (Math.sin(a) * d) + Random.newFloat(-am, am);
		}

		poly = new PolygonSprite(new PolygonRegion(new TextureRegion(textureSolid), shape, triangulator.computeTriangles(shape).toArray()));
		poly.setPosition(x + 8, y + 8);
		poly.setColor(1, 0f, 0f, 1f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.al < 1f) {
			this.al = Math.min(1f, this.al + dt * 4f);
		}
	}

	@Override
	public void render() {
		Graphics.batch.end();

		polyBatch.setProjectionMatrix(Camera.game.combined);
		polyBatch.begin();
		polyBatch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
		poly.draw(polyBatch);
		polyBatch.end();

		polyBatch.setBlendFunction(Graphics.batch.getBlendSrcFunc(), Graphics.batch.getBlendDstFunc());
		Graphics.batch.begin();
	}
}