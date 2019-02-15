package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;

public class MissileAppear extends Entity {
	public MissileProjectile missile;

	{
		depth = -1;
		alwaysRender = true;
		alwaysActive = true;
	}

	@Override
	public void render() {
		if (this.missile.up) {
			return;
		}

		Graphics.batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.setColor(1, 0, 0, 0.4f);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Line);
		Graphics.shape.circle(missile.target.x, missile.target.y, (missile.y - missile.target.y) * 0.1f + 4f);
		Graphics.shape.circle(missile.target.x, missile.target.y, (missile.y - missile.target.y) * 0.1f + 5f);
		Graphics.shape.circle(missile.target.x, missile.target.y, (missile.y - missile.target.y) * 0.1f + 4.5f);

		Graphics.shape.circle(missile.target.x, missile.target.y, 2f);
		Graphics.shape.circle(missile.target.x, missile.target.y, 3f);
		Graphics.shape.circle(missile.target.x, missile.target.y, 2.5f);
		Graphics.endAlphaShape();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (missile.done) {
			done = true;
		}
	}
}