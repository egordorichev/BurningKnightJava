package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Spark;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.BlueHeart;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;

import java.io.IOException;

public class HeartFx extends SaveableEntity {
	private static Animation animations = Animation.make("fx-heart", "-full");
	private AnimationData animation = animations.get("idle");
	private Body body;
	private float t;

	@Override
	public void init() {
		super.init();

		this.w = 12;
		this.h = 9;

		this.t = Random.newFloat(128);
		this.body = World.createSimpleBody(this, 0, 0, this.w, this.h, BodyDef.BodyType.DynamicBody, false);
		
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

				if (player.ui.hasEquiped(BlueHeart.class)) {
					player.modifyMana(2);
				}
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

		if (this.last >= 0.5f) {
			this.last = 0;
			Spark.randomOn(this);
		}

		this.animation.update(dt);
	}

	@Override
	public void render() {
		float a = (float) Math.cos(this.t * 3f) * 8f;
		float sy = (float) (1f + Math.sin(this.t * 2f) / 10f);
		float sx = 1f;

		this.animation.render(this.x, this.y, false, false, this.w / 2, this.h / 2, a, sx, sy);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h);
	}
}