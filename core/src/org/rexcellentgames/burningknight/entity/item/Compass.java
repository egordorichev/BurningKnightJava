package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;

public class Compass extends Item {
	{
		identified = true;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		TextureRegion sprite = this.getSprite();
		Graphics.render(sprite, x + (flipped ? -w / 4 : w / 4) + w / 2, y, 0, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);

		float dx = Exit.instance.x + 8 - x - w / 2;
		float dy = Exit.instance.y + 8 - y - h / 2;
		float a = (float) Math.atan2(dy, dx);

		Graphics.batch.end();
		Graphics.shape.setProjectionMatrix(Camera.game.combined);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
		Graphics.shape.setColor(1, 1, 1, 1);

		float an = (float) Math.toRadians(20);

		Graphics.shape.line((float) (x + w / 2 + Math.cos(a - an) * 18), (float) (y + h / 2 + Math.sin(a - an) * 18), (float) (x + w / 2 + Math.cos(a) * 22), (float) (y + h / 2 + Math.sin(a) * 22));

		Graphics.shape.line((float) (x + w / 2 + Math.cos(a + an) * 18), (float) (y + h / 2 + Math.sin(a + an) * 18), (float) (x + w / 2 + Math.cos(a) * 22), (float) (y + h / 2 + Math.sin(a) * 22));

		Graphics.shape.end();
		Graphics.batch.begin();
	}
}