package org.rexellentgames.dungeon.entity.item.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.geometry.Point;

public class PickupFx extends Entity {
	public TextureRegion region;
	public Point target;
	private float scale = 1f;
	private float t;

	@Override
	public void init() {
		this.alwaysActive = true;
		this.alwaysRender = true;
		this.depth = 30;

		PickupFx self = this;

		Tween.to(new Tween.Task(0, 1f, Tween.Type.LINEAR) {
			@Override
			public float getValue() {
				return scale;
			}

			@Override
			public void setValue(float value) {
				scale = value;
			}

			@Override
			public void onEnd() {
				self.done = true;
			}
		});
	}

	@Override
	public void update(float dt) {
		this.t += dt;

		float dx = target.x - this.x;
		float dy = target.y - this.y;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		if (d > 1) {
			this.x += dx / (d / 2) * dt * 50;
			this.y += dy / (d / 2) * dt * 50;
		}
	}

	@Override
	public void render() {
		Graphics.render(region, this.x + region.getRegionWidth() / 2, this.y + region.getRegionHeight() / 2, this.t * 360f, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, scale, scale);
	}
}