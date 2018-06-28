package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Explosion extends Entity {
	public static Animation boom = Animation.make("explosion");
	private AnimationData animation = boom.get("explosion");
	private float a;
	public float delay;

	{
		depth = 30;
	}

	public Explosion(float x, float y) {
		this.x = x;
		this.y = y;
		this.alwaysActive = true;
	}

	@Override
	public void init() {
		super.init();
		this.a = Random.newFloat(360f);
	}

	@Override
	public void update(float dt) {
		if (this.delay > 0) {
			this.delay -= dt;
			return;
		}

		Dungeon.level.addLightInRadius(this.x, this.y, 1f, 0.7f, 0f, 0.8f, 5f, true);

		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		if (this.delay > 0) {
			return;
		}

		TextureRegion region = this.animation.getCurrent().frame;

		float w = region.getRegionWidth() / 2;
		float h = region.getRegionHeight() / 2;

		this.animation.render(this.x - w, this.y - h, false, false, w, h, this.a);
	}

	public static void make(float x, float y) {
		for (int i = 0; i < Random.newInt(2, 5); i++) {
			Explosion explosion = new Explosion(x + Random.newFloat(-16, 16), y + Random.newFloat(-16, 16));
			explosion.delay = Random.newFloat(0, 0.25f);
			Dungeon.area.add(explosion);
		}


		for (int i = 0; i < Random.newInt(4, 8); i++) {
			Smoke explosion = new Smoke(x + Random.newFloat(-32, 32), y + Random.newFloat(-32, 32));
			explosion.delay = Random.newFloat(0.1f, 0.3f);
			Dungeon.area.add(explosion);
		}


		for (int i = 0; i < Random.newInt(4, 12); i++) {
			TinyParticle explosion = new TinyParticle(x + Random.newFloat(-8, 8), y + Random.newFloat(-8, 8));
			Dungeon.area.add(explosion);
		}

		Camera.shake(10f);
	}
}