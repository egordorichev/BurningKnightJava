package org.rexcellentgames.burningknight.entity.creature.fx;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Note extends Entity {
	public static Animation animations = Animation.make("note");
	private TextureRegion region;
	public float a;
	private Vector2 vel;
	private Body body;
	public boolean bad = true;
	private float t;
	private float scale = 1f;
	public Creature owner;
	private boolean flip;
	private PointLight light;

	{
		alwaysActive = true;
		alwaysRender = true;
	}

	private static Color[] colors = new Color[] {
		Color.valueOf("#3d3d3d"),
		Color.valueOf("#1f6f50"),
		Color.valueOf("#c42430"),
		Color.valueOf("#0069aa")
	};

	@Override
	public void init() {
		this.playSfx("ukulele_" + Random.newInt(1, 5));

		super.init();

		this.flip = Random.chance(50);

		this.vel = new Vector2();

		this.y -= 4;

		vel.x = (float) (Math.cos(this.a) * 60);
		vel.y = (float) (Math.sin(this.a) * 60);

		this.body = World.createSimpleCentredBody(this, 0, 0, 10, 10, BodyDef.BodyType.DynamicBody, true);
	
		if (this.body != null) {
			this.body.setBullet(true);
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
			this.body.setLinearVelocity(this.vel);
		}

		ArrayList<Animation.Frame> frames = animations.getFrames("idle");
		int i = Random.newInt(frames.size());
		region = frames.get(i).frame;

		Color color = colors[i];
		light = new PointLight(World.lights, 32, new Color(color.r * 2, color.g * 2, color.b * 2, 1f), 32, x, y);
	}

	private void parts() {
		for (int i = 0; i < 3; i++) {
			PoofFx part = new PoofFx();

			part.x = this.x;
			part.y = this.y;

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
		light.remove();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void update(float dt) {
		this.t += dt;

		super.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		light.setPosition(x, y);

		if (this.t >= 5f || scale <= 0) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.render(region, this.x, this.y, 0, region.getRegionWidth() / 2, region.getRegionHeight() / 2,
			false, false, flip ? -scale : scale, scale);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x - w / 2 + (flip ? 0 : 4), this.y - this.h / 2 + 3, w - 4, this.h, 8);
	}
}