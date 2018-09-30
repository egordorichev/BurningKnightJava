package org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.MathUtils;
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
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert").readString(),  Gdx.files.internal("shaders/bloom.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	@Override
	public void init() {
		super.init();
		left = Random.chance(50);
		angle = Random.newFloat(360);

		this.r = MathUtils.clamp(0, 1, Random.newFloat(-0.2f, 0.2f) + r);
		this.g = MathUtils.clamp(0, 1, Random.newFloat(-0.2f, 0.2f) + g);
		this.b = MathUtils.clamp(0, 1, Random.newFloat(-0.2f, 0.2f) + b);
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
		Graphics.batch.setColor(r, g, b, a);
		Graphics.render(region, this.x + this.w / 2, this.y + this.h / 2, this.angle, this.w / 2, this.h / 2, false, false, this.scale, this.scale);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}