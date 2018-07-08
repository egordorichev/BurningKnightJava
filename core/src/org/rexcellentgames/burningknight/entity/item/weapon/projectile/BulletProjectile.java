package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.Slab;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.Turret;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BulletProjectile extends Projectile {
	private static TextureRegion burst = Graphics.getTexture("bullet-thrust");
	public TextureRegion sprite;
	public float a;
	private float ra;
	public boolean remove;
	public String letter;
	public boolean circleShape;
	public boolean rotates;
	public static Animation animation = Animation.make("fx-badbullet");
	public Class<? extends Buff> toApply;
	public float duration = 1f;
	public boolean parts;
	public int dir;
	public Point ivel;
	public float angle;
	public float dist;
	public boolean dissappearWithTime;
	public float rotationSpeed = 1f;
	public Gun gun;

	{
		alwaysActive = true;
		depth = 1;
	}

	@Override
	public void init() {
		angle = (float) Math.atan2(this.vel.y, this.vel.x);
		dist = (float) Math.sqrt(vel.x * vel.x + vel.y * vel.y);

		this.ivel = new Point(this.vel.x, this.vel.y);
		this.dir = Random.chance(50) ? -1 : 1;
		this.ra = (float) Math.toRadians(this.a);

		if (this.sprite == null && this.letter != null) {
			this.sprite = Graphics.getTexture("bullet-" + this.letter);
		}

		if (this.sprite != null && this.anim == null) {
			this.w = sprite.getRegionWidth();
			this.h = sprite.getRegionHeight();
		}

		if ((this.w == this.h || circleShape) && !rectShape) {
			this.body = World.createCircleCentredBody(this, 0, 0, (float) Math.ceil((this.h) / 2), BodyDef.BodyType.DynamicBody, this.letter != null && this.letter.equals("bone"));
		} else {
			this.body = World.createSimpleCentredBody(this, 0, 0, this.w, this.h, BodyDef.BodyType.DynamicBody, false);
		}

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, ra);
			this.body.setBullet(true);
		}

		if (this.letter.equals("bone")) {
			this.depth = 16;
		}

		penetrates = !canBeRemoved;
	}

	public boolean rectShape;
	public boolean canBeRemoved = true;

	public void countRemove() {

	}

	@Override
	public void destroy() {
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		TextureRegion reg = this.t < 0.02f ? burst : sprite;
		Texture texture = reg.getTexture();

		Graphics.batch.end();

		RectFx.shader.begin();
		RectFx.shader.setUniformf("r", 1f);
		RectFx.shader.setUniformf("g", 1f);
		RectFx.shader.setUniformf("b", 1f);
		RectFx.shader.setUniformf("a", 1f);

		RectFx.shader.setUniformf("pos", new Vector2(((float) reg.getRegionX()) / texture.getWidth(), ((float) reg.getRegionY()) / texture.getHeight()));
		RectFx.shader.setUniformf("size", new Vector2(((float) reg.getRegionWidth()) / texture.getWidth(), ((float) reg.getRegionHeight()) / texture.getHeight()));

		RectFx.shader.end();

		Graphics.batch.setShader(RectFx.shader);
		Graphics.batch.begin();

		if (this.anim != null) {
			this.anim.render(this.x - 8, this.y - 8, false, false, 8, 8, 0);
		} else {
			Graphics.render(reg, this.x, this.y, this.a, reg.getRegionWidth() / 2, reg.getRegionHeight() / 2, false, false);
		}

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h, 5);
	}

	protected float last;

	@Override
	protected boolean breaksFrom(Entity entity) {
		return this.canBeRemoved && (entity == null || (entity instanceof SolidProp && !(entity instanceof Turret || entity instanceof Slab)) || entity instanceof Door);
	}

	@Override
	protected boolean hit(Entity entity) {
		if (this.bad) {
			if (entity instanceof Player) {
				this.doHit(entity);
				return this.canBeRemoved;
			}
		} else if (entity instanceof Mob) {
			this.doHit(entity);
			return this.canBeRemoved;
		}

		return false;
	}

	@Override
	protected void doHit(Entity entity) {
		super.doHit(entity);

		if (this.owner != null && Random.chance(this.owner.getStat("slow_down_on_hit") * 100)) {
			Dungeon.slowDown(0.5f, 0.5f);
		}
	}

	@Override
	protected void onHit(Entity entity) {
		if (toApply != null) {
			try {
				((Creature) entity).addBuff(toApply.newInstance().setDuration(this.duration));
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public AnimationData anim;

	@Override
	public void logic(float dt) {
		this.last += dt;

		if (this.anim != null) {
			this.anim.update(dt);
		}

		if (this.dissappearWithTime && this.t >= 5f) {
			this.death();
			this.remove = true;
			this.broke = true;
		}

		if (this.parts) {
			if (this.last > 0.08f) {
				this.last = 0;
				Part part = new Part();
				part.vel = new Point();

				part.x = this.x + Random.newFloat(this.sprite.getRegionWidth()) - this.sprite.getRegionWidth()/ 2 - 4;
				part.y = this.y + Random.newFloat(this.sprite.getRegionHeight()) - this.sprite.getRegionHeight() / 2 - 4;
				part.depth = -1;
				part.animation = animation.get("idle");

				Dungeon.area.add(part);
			}
		}

		boolean dd = this.done;
		this.done = this.remove;

		if (this.done && !dd) {
			this.onDeath();

			for (int i = 0; i < 20; i++) {
				Part part = new Part();

				part.x = this.x - this.vel.x * dt;
				part.y = this.y - this.vel.y * dt;

				Dungeon.area.add(part);
			}
		}

		this.ra = (float) Math.atan2(this.vel.y, this.vel.x);

		if (this.rotates) {
			this.a += dt * 360 * 2 * dir * rotationSpeed;
		} else {
			this.a = (float) Math.toDegrees(this.ra);
		}

		this.control();

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.body.setTransform(this.x, this.y, this.ra);
		this.body.setLinearVelocity(this.vel);
	}

	@Override
	protected void onDeath() {
		super.onDeath();

		if (this.owner != null && Random.chance(this.owner.getStat("ammo_restore_chance_on_lost") * 100)) {
			this.gun.setAmmoLeft(this.gun.getAmmoLeft() + 1);
		}
	}

	public void control() {

	}
}