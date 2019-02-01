package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.ice.IceElemental;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.SimplePart;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.Slab;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.pattern.BulletPattern;
import org.rexcellentgames.burningknight.entity.trap.Turret;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BulletProjectile extends Projectile {
	private static TextureRegion burst = Graphics.getTexture("bullet-thrust");
	public TextureRegion sprite;
	public float a;
	public float ra;
	public boolean remove;
	public String letter;
	public boolean circleShape;
	public boolean rotates;
	public Class<? extends Buff> toApply;
	public float duration = 2f;
	public boolean parts;
	public int dir;
	public Point ivel;
	public float angle;
	public float dist;
	public boolean dissappearWithTime;
	public float rotationSpeed = 1f;
	public Gun gun;
	public boolean second = true;
	public boolean lightUp = true;
	public boolean auto = false;
	public boolean renderCircle = true;
	public boolean brokeWeapon = false;
	public float alp = 1f;
	public int bounce;

	protected PointLight light;

	{
		alwaysActive = true;
		depth = 16;
	}

	public boolean noRotation;
	public float delay;

	@Override
	public void init() {
		angle = (float) Math.atan2(this.velocity.y, this.velocity.x);
		dist = (float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);

		this.ivel = new Point(this.velocity.x, this.velocity.y);
		this.dir = Random.chance(50) ? -1 : 1;
		this.ra = (float) Math.toRadians(this.a);

		if (this.sprite == null && this.letter != null) {
			if (this.letter.startsWith("item")) {
				lightUp = false;
				rotationSpeed = 0.7f;
				this.sprite = Graphics.getTexture(this.letter);
			} else {
				this.sprite = Graphics.getTexture("bullet-" + this.letter.replace("bullet-", ""));
			}
		}

		if (this.sprite != null && this.anim == null) {
			this.w = sprite.getRegionWidth();
			this.h = sprite.getRegionHeight();
		}

		if (!noLight) {
			light = World.newLight(32, new Color(1, 0, 0, 1f), 64, x, y, false);

			if (letter != null) {
				switch (this.letter) {
					case "bullet-a":
					case "bullet-missile":
						light.setColor(1, 1, 0, 1);
						break;
					case "bullet-bill":
						light.setColor(0, 1, 0.3f, 1);
						lightUp = false;
						break;
					case "bullet-snow":
						light.setColor(0.5f, 1, 1, 1);
						break;
				}
			}
		}

		if (this.letter != null) {
			if (this.letter.equals("bullet-rekt")) {
				noRotation = false;
			} else if (this.letter.equals("bullet-bone")) {
				this.depth = 16;
				second = false;
			} else if (this.letter.equals("bullet-bad") || this.letter.equals("bullet-rect") || this.letter.equals("bullet-nano") || this.letter.equals("bullet-atom") || this.letter.equals("bullet-A") || this.letter.equals("bullet-h")) {
				this.noRotation = true;
				this.second = false;
			} else if (this.letter.equals("bullet-snow")) {
				this.rotates = true;
				this.second = false;
			} else if (this.letter.equals("bullet-skull")) {
				this.rotates = false;
				this.second = false;
				this.noRotation = true;
				this.rectShape = false;
				this.circleShape = true;
				this.renderCircle = false;
				this.dissappearWithTime = true;
				this.lightUp = false;
			} else if (this.letter.equals("bullet-kotlin")) {
				this.second = false;
				lightUp = false;
			} else if (this.letter.equals("bullet-bolt")) {
				second = false;
				lightUp = false;
			} else if (this.letter.equals("bullet-book")) {
				lightUp = false;
				second = false;
				rotates = true;
				rotationSpeed = 0.3f;
			}
		}

		if ((this.w == this.h || circleShape) && !rectShape) {
			this.body = World.createCircleCentredBody(this, 0, 0, (float) Math.ceil((this.h) / 2), BodyDef.BodyType.DynamicBody, bounce == 0);
		} else {
			this.body = World.createSimpleCentredBody(this, 0, 0, this.w, this.h, BodyDef.BodyType.DynamicBody, bounce == 0);
		}

		body.getFixtureList().get(0).setRestitution(1f);
		body.setTransform(x, y, ra);
		body.setBullet(true);
		body.setLinearVelocity(this.velocity);

		penetrates = !canBeRemoved;
	}

	public boolean rectShape;
	public boolean canBeRemoved = true;

	public boolean noLight;

	public void countRemove() {

	}

	@Override
	public void destroy() {
		this.body = World.removeBody(this.body);
		World.removeLight(light);
	}

	@Override
	public void render() {
		TextureRegion reg = (renderCircle && this.t < 0.05f) ? burst : sprite;
		Texture texture = reg.getTexture();

		Graphics.batch.end();
		RectFx.shader.begin();
		RectFx.shader.setUniformf("white", (this.dissappearWithTime && this.t >= ds && (this.t - ds) % 0.3f > 0.15f) ? 1 : 0);
		alp = 1;

		RectFx.shader.setUniformf("r", 1f);
		RectFx.shader.setUniformf("g", 1f);
		RectFx.shader.setUniformf("b", 1f);
		RectFx.shader.setUniformf("a", (second ? 0.33f : 1f) * alp);
		RectFx.shader.setUniformf("remove", (!lightUp || second) ? 1f : 0f);

		RectFx.shader.setUniformf("pos", new Vector2(((float) reg.getRegionX()) / texture.getWidth(), ((float) reg.getRegionY()) / texture.getHeight()));
		RectFx.shader.setUniformf("size", new Vector2(((float) reg.getRegionWidth()) / texture.getWidth(), ((float) reg.getRegionHeight()) / texture.getHeight()));

		RectFx.shader.end();

		Graphics.batch.setShader(RectFx.shader);
		Graphics.batch.begin();

		if (this.anim != null) {
			if (second) {
				this.anim.render(this.x - 8, this.y - 8, false, false, 8, 8, 0, 2, 2);
				Graphics.batch.end();
				RectFx.shader.begin();
				RectFx.shader.setUniformf("a", alp);
				RectFx.shader.setUniformf("remove", 0f);
				RectFx.shader.end();
				Graphics.batch.begin();
			}

			this.anim.render(this.x - 8, this.y - 8, false, false, 8, 8, 0);
		} else {
			if (second) {
				Graphics.render(reg, this.x, this.y, this.noRotation ? 0 : this.a, reg.getRegionWidth() / 2, reg.getRegionHeight() / 2, false, false, 2, 2);
				Graphics.batch.end();
				RectFx.shader.begin();
				RectFx.shader.setUniformf("a", alp);
				RectFx.shader.setUniformf("remove", 0f);
				RectFx.shader.end();
				Graphics.batch.begin();
			}

			Graphics.render(reg, this.x, this.y, this.noRotation ? 0 : this.a, reg.getRegionWidth() / 2, reg.getRegionHeight() / 2, false, false);
		}

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
		Graphics.batch.setColor(1, 1, 1, 1);

	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h, 5);
	}

	protected float last;

	@Override
	public void onCollision(Entity entity) {
		if (this.alp < 1) {
			return;
		}

		super.onCollision(entity);

		if (this.bad && entity instanceof WeaponBase && ((WeaponBase) entity).getOwner() instanceof Player) {
			if (auto) {
				this.broke = true;
				this.brokeWeapon = true;
			} else {
				Player player = (Player) ((WeaponBase) entity).getOwner();
				double a = this.getAngleTo(player.x + player.w / 2, player.y + player.h / 2) - Math.PI;
				double d = Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);

				if (pattern != null) {
					pattern.removeBullet(this);
					ignoreVel = false;
					ignoreBodyPos = false;
					d = Math.sqrt(pattern.velocity.x * pattern.velocity.x + pattern.velocity.y * pattern.velocity.y);
					Dungeon.area.add(this, true);
				}

				this.velocity.x = (float) (d * Math.cos(a));
				this.velocity.y = (float) (d * Math.sin(a));
				this.bad = false;

				int num = GlobalSave.getInt("num_bullets_reflected") + 1;
				GlobalSave.put("num_bullets_reflected", num);

				if (num >= 30) {
					Achievements.unlock(Achievements.UNLOCK_AMMO_ORBITAL);
				}

				if (this.body != null) {
					this.body.setLinearVelocity(this.velocity);
				}

				for (int i = 0; i < 3; i++) {
					PoofFx fx = new PoofFx();

					fx.x = this.x;
					fx.y = this.y;

					Dungeon.area.add(fx);
				}
			}
		} else if (this.bad && this.auto && entity instanceof BulletProjectile && (((BulletProjectile) entity).owner instanceof Player)) {
			this.broke = true;
			((BulletProjectile) entity).broke = true;
		} else if (this.bad && entity instanceof Player) {
			this.brokeWeapon = true;
		}
	}

	@Override
	protected boolean breaksFrom(Entity entity) {
		return this.canBeRemoved && ((entity == null) || (entity instanceof SolidProp && !(entity instanceof Turret || entity instanceof Slab)) || entity instanceof Door);
	}

	@Override
	public void brak() {
		bounce --;

		if (bounce < 0) {
			super.brak();
		}
	}

	@Override
	protected boolean hit(Entity entity) {
		if (this.bad) {
			if (entity instanceof Player && !((Player) entity).isRolling()) {
				this.doHit(entity);
				return this.canBeRemoved;
			}
		} else if (entity instanceof Mob) {
			if (!(entity instanceof IceElemental)) {
				this.doHit(entity);
			} else {
				return false;
			}

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

	public float ds = 4f;

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
		if (this.delay > 0) {
			World.checkLocked(this.body).setTransform(this.x, this.y, this.ra);

			if (!noLight) {
				light.setPosition(x, y);
			}

			this.delay -= dt;
			return;
		}

		this.last += dt;

		if (this.anim != null) {
			this.anim.update(dt);
		}

		if (this.dissappearWithTime && this.t >= ds + 1) {
			this.death();
			this.remove = true;
			this.broke = true;
		}

		if (this.parts) {
			if (this.last > 0.08f) {
				this.last = 0;
				SimplePart part = new SimplePart();
				part.vel = new Point();

				part.x = this.x + Random.newFloat(this.sprite.getRegionWidth() / 2) - this.sprite.getRegionWidth() / 4;
				part.y = this.y + Random.newFloat(this.sprite.getRegionHeight() / 2) - this.sprite.getRegionHeight() / 4;
				part.depth = -1;

				Dungeon.area.add(part);
			}
		}

		boolean dd = this.done;
		this.done = this.remove;

		if (this.done && !dd) {
			this.onDeath();

			for (int i = 0; i < 20; i++) {
				Part part = new Part();

				part.x = this.x - this.velocity.x * dt;
				part.y = this.y - this.velocity.y * dt;

				Dungeon.area.add(part);
			}
		}

		if (this.rotates) {
			this.a += dt * 360 * 2 * dir * rotationSpeed;
		}

		this.control();

		if (this.auto) {
			float dx = Player.instance.x + Player.instance.w / 2 - this.x - 5;
			float dy = Player.instance.y + Player.instance.h / 2 - this.y - 5;
			float angle = (float) Math.atan2(dy, dx);

			this.angle = Gun.angleLerp(this.angle, angle, dt * 2f, false);

			float f = 60f;
			this.velocity.x = (float) (Math.cos(this.angle) * f);
			this.velocity.y = (float) (Math.sin(this.angle) * f);
		}

		if (delay <= 0 && pattern == null) {
			this.x += this.velocity.x * dt;
			this.y += this.velocity.y * dt;

			//this.body.setLinearVelocity(this.velocity);
		}

		//World.checkLocked(this.body).setTransform(this.x, this.y, this.ra);

		if (!noLight) {
			light.setPosition(x, y);
		}
	}

	public BulletPattern pattern;

	@Override
	protected void onDeath() {
		super.onDeath();

		if (this.owner != null && Random.chance(this.owner.getStat("ammo_restore_chance_on_lost") * 100)) {
			this.gun.setAmmoLeft(this.gun.getAmmoLeft() + 1);
		}
	}

	public void control() {

	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity == null) {
			return true;
		}

		if (entity instanceof Orbital) {
			return true;
		}

		if (!((entity instanceof BulletProjectile && ((Projectile) entity).bad != this.bad))) {
			return false;
		}

		return super.shouldCollide(null, contact, fixture);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.body != null) {
			this.velocity.x = this.body.getLinearVelocity().x;
			this.velocity.y = this.body.getLinearVelocity().y;

			ra = (float) Math.atan2(this.velocity.y, this.velocity.x);
			a = (float) Math.toDegrees(ra);
			body.setTransform(x, y, ra);
		}
	}
}