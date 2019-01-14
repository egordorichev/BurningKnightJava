package org.rexcellentgames.burningknight.entity.creature.fx;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class ManaFx extends SaveableEntity {
	public boolean half;
	public Body body;
	private PointLight light;

	private AnimationData anim;
	private static Animation fullAnim = Animation.make("fx-star", "-full");
	private static Animation halfAnim = Animation.make("fx-star", "-half");
	private float waitT;

	@Override
	public void renderShadow() {
		Graphics.shadow(x, y, w, h);
	}

	@Override
	public void init() {
		super.init();
		waitT = 0.5f;

		anim = half ? halfAnim.get("idle") : fullAnim.get("idle");
		anim.randomize();
		TextureRegion region = anim.getFrames().get(0).frame;

		this.w = 14;
		this.h = region.getRegionHeight();

		x += Random.newFloat(4) - 2;
		y += Random.newFloat(4) - 2;

		body = World.createCircleBody(this, 0, 0, Math.min(w, h) / 2, BodyDef.BodyType.DynamicBody, false);
		body.setTransform(this.x, this.y, 0);

		light = World.newLight(32, new Color(0, 1, 1, 1), 64, 0, 0);
		light.setPosition(this.x + w / 2, this.y + h / 2);
	}

	public void poof() {
		for (int i = 0; i < 3; i++) {
			PoofFx fx = new PoofFx();

			fx.t = 0.5f;
			fx.x = this.x + this.w / 2;
			fx.y = this.y + this.h / 2;

			Dungeon.area.add(fx);
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(half);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		half = reader.readBoolean();
		waitT = 0;
		body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void render() {
		anim.render(this.x, this.y, false);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.join) {
			this.done = true;
			this.poof();
			return;
		}

		if (this.make) {
			this.poof();

			make = false;
			half = false;
			body = World.removeBody(body);
			anim = fullAnim.get("idle");
			anim.randomize();
			TextureRegion region = anim.getFrames().get(0).frame;

			this.w = region.getRegionWidth();
			this.h = region.getRegionHeight();

			body = World.createCircleBody(this, 0, 0, Math.min(w, h) / 2, BodyDef.BodyType.DynamicBody, false);
			body.setTransform(this.x, this.y, 0);
		}

		anim.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		Vector2 vel = this.body.getLinearVelocity();
		vel.x -= vel.x * dt * 3;
		vel.y -= vel.y * dt * 3;

		if (waitT > 0) {
			waitT -= dt;
		}

		light.setPosition(this.x + w / 2, this.y + h / 2);
		boolean force = (Player.instance.room != null && Player.instance.room.lastNumEnemies == 0 && Player.instance.getManaMax() - Player.instance.getMana() > 0) || Dungeon.level.checkFor(Math.round(this.x / 16), Math.round(this.y / 16), Terrain.HOLE);

		if ((waitT <= 0 && Player.instance.getManaMax() - Player.instance.getMana() > 0 || force) && !Player.instance.isDead()) {
			float dx = Player.instance.x + 8 - this.x - this.w / 2;
			float dy = Player.instance.y + 8 - this.y - this.h / 2;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < 48 || force) {
				float f = 1024;
				vel.x += dx / d * dt * f;
				vel.y += dy / d * dt * f;
			}
		}

		this.body.setLinearVelocity(vel);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player) {
			Player player = (Player) entity;

			if (player.getManaMax() - player.getMana() > 0) {
				player.modifyMana(half ? 1 : 2);
				done = true;

				poof();
			}
		} else if (entity instanceof ManaFx) {
			if (((ManaFx) entity).half && this.half && !((ManaFx) entity).join) {
				this.join = true;
				((ManaFx) entity).make = true;
			}
		}
	}

	public boolean make;
	public boolean join;

	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(light);
		body = World.removeBody(body);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof Mob || entity instanceof WeaponBase || entity instanceof Level || (entity instanceof Player && ((Player) entity).isRolling())
			|| (entity == null && Player.instance.room != null && Player.instance.room.lastNumEnemies == 0)) {

			return false;
		}

		if (entity instanceof ManaFx) {
			return true;
		}

		return super.shouldCollide(entity, contact, fixture);
	}

	{
		alwaysActive = true;
	}
}