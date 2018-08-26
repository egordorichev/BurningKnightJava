package org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class RectFx extends Entity {
	public float r = 1;
	public float g = 1;
	public float b = 1;
	public float a = 1;
	public float scale = 1;
	public float angle;
	public static TextureRegion region = Graphics.getTexture("particle-rect");

	{
		alwaysActive = true;
	}

	private boolean left;

	public static ShaderProgram shader;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/bloom.vert").readString(),  Gdx.files.internal("shaders/bloom.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	@Override
	public void init() {
		super.init();
		left = Random.chance(50);
		angle = Random.newFloat(360);

		this.r += Random.newFloat(-0.2f, 0.2f);
		this.g += Random.newFloat(-0.2f, 0.2f);
		this.b += Random.newFloat(-0.2f, 0.2f);
	}

	@Override
	public void update(float dt) {
		this.a -= dt / 4;
		this.angle += (this.left ? dt : -dt) * 360;
		this.scale -= dt;

		if (this.a <= 0 || this.scale <= 0) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.batch.end();
		RectFx.shader.begin();
		RectFx.shader.setUniformf("r", this.r);
		RectFx.shader.setUniformf("g", this.g);
		RectFx.shader.setUniformf("b", this.b);
		RectFx.shader.setUniformf("a", this.a);

		Texture texture = region.getTexture();

		RectFx.shader.setUniformf("pos", new Vector2(((float) region.getRegionX()) / texture.getWidth(), ((float) region.getRegionY()) / texture.getHeight()));
		RectFx.shader.setUniformf("size", new Vector2(((float) region.getRegionWidth()) / texture.getWidth(), ((float) region.getRegionHeight()) / texture.getHeight()));

		RectFx.shader.end();

		Graphics.batch.setShader(RectFx.shader);
		Graphics.batch.begin();
		Graphics.render(region, this.x + this.w / 2, this.y + this.h / 2, this.angle, this.w / 2, this.h / 2, false, false, this.scale, this.scale);
		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}
}