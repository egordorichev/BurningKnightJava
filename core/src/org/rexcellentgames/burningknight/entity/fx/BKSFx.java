package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class BKSFx extends Entity {
	{
		alwaysActive = true;
		depth = -1;
	}

	public TextureRegion region;
	public float a;
	public float ox;
	public float oy;
	private float s = 1;
	public boolean flipped;

	@Override
	public void update(float dt) {
		super.update(dt);

		s -= dt;

		if (s <= 0) {
			done = true;
		}
	}

	@Override
	public void render() {

		Graphics.batch.end();
		Mob.shader.begin();
		Mob.shader.setUniformf("u_color", new Vector3(1, 0.3f, 0.3f));
		Mob.shader.setUniformf("u_a", 0.8f);
		Mob.shader.end();
		Graphics.batch.setShader(Mob.shader);
		Graphics.batch.begin();

		Graphics.render(region, x, y, a, ox, oy, false, false, flipped ? -s : s, s);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}
}