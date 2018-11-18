package org.rexcellentgames.burningknight.entity.fx;

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
		Graphics.shape.begin(ShapeRenderer.ShapeType.Line);
		Graphics.shape.setColor(1, 0, 0, 1);
		Graphics.shape.circle(missile.target.x, missile.target.y, (missile.y - missile.target.y) * 0.1f + 4f);
		Graphics.shape.circle(missile.target.x, missile.target.y, 2f);
		Graphics.endShape();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (missile.done) {
			done = true;
		}
	}
}