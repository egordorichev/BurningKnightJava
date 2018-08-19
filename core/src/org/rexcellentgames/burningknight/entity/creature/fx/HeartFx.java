package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
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
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class HeartFx extends SaveableEntity {
	private static Animation animations = Animation.make("fx-heart", "-full");
	private static Animation halfAnim = Animation.make("fx-heart", "-half");

	private AnimationData animation;
	private Body body;
	private float t;
	private boolean half;

	@Override
	public void init() {
		super.init();

		this.half = Random.chance(50);
		this.w = 12;
		this.h = 9;
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.half = reader.readBoolean();

		if (this.half) {
			this.w /= 2;
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(this.half);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player) {
			Player player = ((Player) entity);

			if (player.getHp() < player.getHpMax()) {
				player.modifyHp(2, null);
				this.done = true;
				player.playSfx("health_up");

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

	private float last;

	@Override
	public void update(float dt) {
		if (this.body == null) {
			this.t = Random.newFloat(128);
			this.body = World.createCircleBody(this, 0, 0, Math.max(this.h / 2, this.w / 2), BodyDef.BodyType.DynamicBody, false, 0.8f);

			MassData data = new MassData();
			data.mass = 0.1f;
			this.body.setMassData(data);
			this.body.setTransform(this.x, this.y, 0);
		}

		super.update(dt);

		this.t += dt;
		this.last += dt;

		if (this.last >= 0.5f) {
			this.last = 0;
			Spark.randomOn(this);
		}

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.vel.x = this.body.getLinearVelocity().x;
		this.vel.y = this.body.getLinearVelocity().y;

		this.vel.x *= 0.96f;
		this.vel.y *= 0.96f;

		this.body.setLinearVelocity(this.vel);

		if (this.animation == null) {
			this.animation = this.half ? halfAnim.get("idle") : animations.get("idle");
		}

		this.animation.update(dt);
	}

	@Override
	public void render() {
		float a = (float) Math.cos(this.t * 3f) * 8f;
		float sy = (float) (1f + Math.sin(this.t * 2f) / 10f);
		float sx = 1f;

		if (this.animation != null) {
			this.animation.render(this.x, this.y, false, false, this.w / 2, this.h / 2, a, sx, sy);
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h);
	}
}