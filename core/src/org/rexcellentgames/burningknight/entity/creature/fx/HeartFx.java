package org.rexcellentgames.burningknight.entity.creature.fx;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Spark;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class HeartFx extends SaveableEntity {
	private static Animation red = Animation.make("fx-heart", "-full");
	private static Animation redHalf = Animation.make("fx-heart", "-half");
	private static Animation iron = Animation.make("fx-heart", "-iron");
	private static Animation golden = Animation.make("fx-heart", "-golden");

	private AnimationData animation;
	private Body body;
	private float t;
	private Type type;
	private PointLight light;

	public enum Type {
		RED(0, false), RED_HALF(1, true),
		IRON(2, false),
		GOLDEN(3, false);

		public byte id;
		public boolean half;

		Type(int id, boolean half) {
			this.id = (byte) id;
			this.half = half;
		}
	}

	{
		this.w = 12;
		this.h = 9;
	}

	public void randomVelocity() {
		float a = Random.newFloat((float) (Math.PI * 2f));
		float f = Random.newFloat(60f, 150f);

		this.velocity.x = (float) (Math.cos(a) * f);
		this.velocity.y = (float) (Math.sin(a) * f);

		if (body != null) {
			body.setLinearVelocity(velocity);
		}
	}

	@Override
	public void init() {
		super.init();
		light = World.newLight(16, new Color(1, 0.2f, 0, 1f), 64, x, y);

		this.generate();
	}

	public void generate() {
		if (Dungeon.depth == -1) {
			this.type = Type.RED;
		} else {
			float r = Random.newFloat(1f);

			if (r < 0.5f) {
				this.type = Type.RED_HALF;
			} else if (r < 0.85) {
				this.type = Type.RED;
			} else if (r < 0.98) {
				this.type = Type.IRON;
			} else {
				this.type = Type.GOLDEN;
			}
		}

		setColor();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.type = Type.values()[reader.readByte()];

		if (this.type.half) {
			// this.w /= 2;
		}

		setColor();
	}

	private void setColor() {
		switch (this.type) {
			case RED: case RED_HALF: this.light.setColor(1, 0.1f, 0, 1f); break;
			case GOLDEN: this.light.setColor(1, 1f, 0, 1f); break;
			case IRON: this.light.setColor(1, 1f, 1, 1f); break;
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeByte(this.type.id);
	}

	@Override
	public void onCollision(Entity entity) {
		if (t <= 0.04f) {
			return;
		}

		if (entity instanceof Player) {
			Player player = ((Player) entity);

			int s = Player.instance.getHpMax() + Player.instance.getGoldenHearts() + Player.instance.getIronHearts();

			if ((this.type == Type.RED || this.type == Type.RED_HALF) && player.getHp() < player.getHpMax()) {
				player.modifyHp(2, null);
				this.end(player);

				player.numCollectedHearts += this.type == Type.RED ? 2 : 1;
			} else if (s < 32) {
				if (this.type == Type.GOLDEN) {
					Achievements.unlock(Achievements.UNLOCK_DIAMOND);
					player.addGoldenHearts(s == 31 ? 1 : 2);
					player.numCollectedHearts += 2;
					this.end(player);
				} else if (this.type == Type.IRON) {
					player.addIronHearts(s == 31 ? 1 : 2);
					this.end(player);
					player.numCollectedHearts += 2;
				}
			}
		}
	}

	private void end(Player player) {
		this.done = true;
		player.playSfx("health_up");

		for (int i = 0; i < 3; i++) {
			PoofFx fx = new PoofFx();

			fx.x = this.x + this.w / 2;
			fx.y = this.y + this.h / 2;

			Dungeon.area.add(fx);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(light);
		this.body = World.removeBody(this.body);
	}

	private float last;

	private boolean falling;
	private float fall = 1;

	private void checkFall() {
		if (falling) {
			return;
		}

		for (int x = (int) Math.floor((this.x) / 16); x < Math.ceil((this.x + 16) / 16); x++) {
			for (int y = (int) Math.floor((this.y + 8) / 16); y < Math.ceil((this.y + 16) / 16); y++) {
				if (x < 0 || y < 0 || x >= Level.getWidth() || y >= Level.getHeight()) {
					continue;
				}

				if (CollisionHelper.check(this.x, this.y, 16, 8, x * 16, y * 16 - 8, 16, 16)) {
					int i = Level.toIndex(x, y);
					byte t = Dungeon.level.get(i);

					if (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C || t == Terrain.FLOOR_D) {
						return;
					}
				}
			}
		}

		falling = true;
	}

	@Override
	public void update(float dt) {
		this.light.setPosition(x, y);
		checkFall();

		if (falling) {
			fall -= dt;

			if (fall <= 0) {
				done = true;
			}
		}

		if (this.body == null) {
			this.t = Random.newFloat(128);
			this.body = World.createCircleBody(this, 0, 0, Math.max(this.h / 2, this.w / 2), BodyDef.BodyType.DynamicBody, false, 0.8f);
			body.setLinearVelocity(this.velocity);
			MassData data = new MassData();
			data.mass = 0.1f;
			this.body.setMassData(data);
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
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

		this.velocity.x = this.body.getLinearVelocity().x;
		this.velocity.y = this.body.getLinearVelocity().y;

		this.velocity.x -= this.velocity.x * Math.min(1, dt * 3);
		this.velocity.y -= this.velocity.y * Math.min(1, dt * 3);


		this.body.setLinearVelocity(this.velocity);

		if (this.animation == null) {
			switch (this.type) {
				case RED: this.animation = red.get("idle"); break;
				case RED_HALF: this.animation = redHalf.get("idle"); break;
				case IRON: this.animation = iron.get("idle"); break;
				case GOLDEN: this.animation = golden.get("idle"); break;
			}
		}

		this.animation.update(dt * 0.6f);
	}

	@Override
	public void render() {
		float a = (float) Math.cos(this.t * 3f) * 8f;
		float sy = (float) (1f + Math.sin(this.t * 2f) / 10f) * fall;
		float sx = fall;

		if (this.animation != null) {
			this.animation.render(this.x, this.y, false, false, this.w / 2, this.h / 2, a, sx, sy);
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w * fall, this.h * fall);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (!(entity instanceof HeartFx || entity instanceof SolidProp) && entity != null) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}
}