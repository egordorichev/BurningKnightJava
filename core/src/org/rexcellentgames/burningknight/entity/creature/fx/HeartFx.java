package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Spark;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;

import java.io.IOException;
import java.util.ArrayList;

public class HeartFx extends SaveableEntity {
	private static Animation animations = Animation.make("fx-heart");
	private TextureRegion region;
	private Body body;
	private float t;

	@Override
	public void init() {
		super.init();

		ArrayList<Animation.Frame> frames = animations.getFrames("idle");
		this.region = frames.get(Random.newInt(frames.size())).frame;

		this.w = this.region.getRegionWidth();
		this.h = this.region.getRegionHeight();

		this.t = Random.newFloat(128);
		this.body = World.createSimpleBody(this, 0, 0, this.w, this.h, BodyDef.BodyType.DynamicBody, true);
		
		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player) {
			Player player = ((Player) entity);

			if (player.getHp() < player.getHpMax()) {
				player.modifyHp(2, null);
				this.done = true;
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	float last;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;
		this.last += dt;

		if (this.last >= 0.2f) {
			this.last = 0;
			Spark.randomOn(this);
		}
	}

	@Override
	public void render() {
		float a = (float) Math.cos(this.t * 3f) * 8f;
		float sy = (float) (1f + Math.sin(this.t * 2f) / 10f);
		float sx = 1f;

		Graphics.render(this.region, this.x + this.w / 2, this.y + this.h / 2, a, this.w / 2, this.h / 2, false, false, sx, sy);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h);
	}
}