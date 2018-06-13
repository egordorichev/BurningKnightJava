package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Note extends Entity implements WormholeFx.Suckable {
	public static Animation animations = Animation.make("note");
	private TextureRegion region;
	public float a;
	private Vector2 vel;
	private Body body;
	public boolean bad = true;
	private float t;
	private float scale = 1f;
	public Creature owner;

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public void init() {
		this.playSfx("ukulele_" + Random.newInt(1, 5));

		super.init();

		this.vel = new Vector2();

		WormholeFx.suck.add(this);
		this.y -= 4;

		vel.x = (float) (Math.cos(this.a) * 60);
		vel.y = (float) (Math.sin(this.a) * 60);

		this.body = World.createSimpleBody(this, 0, 0, 10, 10, BodyDef.BodyType.DynamicBody, true);
	
		if (this.body != null) {
			this.body.setBullet(true);
			this.body.setTransform(this.x, this.y, 0);
			this.body.setLinearVelocity(this.vel);
		}

		ArrayList<Animation.Frame> frames = animations.getFrames("idle");
		region = frames.get(Random.newInt(frames.size())).frame;
	}

	private void parts() {
		for (int i = 0; i < 20; i++) {
			Part part = new Part();

			part.x = this.x - this.vel.x;
			part.y = this.y - this.vel.y;

			Dungeon.area.add(part);
		}

		this.done = true;
	}

	private boolean brk;

	@Override
	public void onCollision(Entity entity) {
		if (this.brk || this.body == null) {
			return;
		}

		if (entity instanceof Mob && !this.bad && !((Mob) entity).isDead()) {
			((Mob) entity).modifyHp(Math.round(Random.newFloatDice(-1, -2)), this.owner, true);
			this.brk = true;
			this.vel.x = 0;
			this.vel.y = 0;
			this.body.setLinearVelocity(this.vel);
			this.parts();
		} else if (entity instanceof Player && this.bad) {
			((Player) entity).modifyHp(Math.round(Random.newFloatDice(-1, -2)), this.owner, true);
			this.brk = true;
			this.vel.x = 0;
			this.vel.y = 0;
			this.body.setLinearVelocity(this.vel);
			this.parts();
		} else if (entity == null) {
			this.brk = true; // Wall
			this.vel.x = 0;
			this.vel.y = 0;
			this.body.setLinearVelocity(this.vel);
			this.parts();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		WormholeFx.suck.remove(this);
		this.body = World.removeBody(this.body);
	}

	@Override
	public void update(float dt) {
		this.t += dt;

		super.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		if (this.t >= 5f || scale <= 0) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.render(region, this.x, this.y, 0, region.getRegionWidth() / 2, region.getRegionHeight() / 2,
			false, false, scale, scale);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x - w / 2 + 4, this.y - this.h / 2 + 5, w - 4, this.h, 5);
	}
}