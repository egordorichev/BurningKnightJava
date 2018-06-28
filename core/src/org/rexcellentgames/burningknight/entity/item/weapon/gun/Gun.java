package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Bullet;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Shell;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.Utils;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class Gun extends WeaponBase {
	private float accuracy = 10f;
	protected float sx = 1f;
	protected float sy = 1f;
	protected float vel = 6f;
	protected Class<? extends Bullet> ammo;
	protected float textureA;
	protected boolean penetrates;
	protected float tw;
	protected float th;
	protected boolean s;
	protected Point origin = new Point(3, 1);
	protected Point hole = new Point(13, 6);
	protected int maxCharge = 100;
	protected int charge = 100;
	protected int ammoMax = 20;
	protected int ammoLeft = 20;
	protected float chargeProgress;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.charge = reader.readInt32();
		this.ammoMax = reader.readInt32();
		this.ammoLeft = reader.readInt32();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt32(this.charge);
		writer.writeInt32(this.ammoMax);
		writer.writeInt32(this.ammoLeft);
	}

	@Override
	public int getValue() {
		return this.ammoLeft;
	}

	public int getMaxCharge() {
		return maxCharge;
	}

	public int getCharge() {
		return charge;
	}

	public int getAmmoMax() {
		return ammoMax;
	}

	public int getAmmoLeft() {
		return ammoLeft;
	}

	{
		identified = true;
		auto = true;
		useTime = 0.2f;
	}

	private Vector2 last = new Point();
	private float lastAngle;

	private float closestFraction = 1.0f;
	
	protected Gun() {
	  String unlocalizedName = Utils.INSTANCE.pascalCaseToSnakeCase(getClass().getSimpleName());
	  
	  this.name = Locale.get(unlocalizedName);
	  this.description = Locale.get(unlocalizedName + "_desc");
	  this.sprite = "item-" + unlocalizedName;
  }

	@Override
	public void updateInHands(float dt) {
		super.updateInHands(dt);

		if (this.ammoLeft == 0) {
			if (this.chargeProgress == 0) {
				this.time = 1f / this.owner.getStat("reload_time");
			}

			this.chargeProgress += dt / 3f;

			if (this.chargeProgress >= 1f) {
				this.ammoLeft = this.ammoMax;
				this.charge -= this.ammoMax;

				// todo: what if no left?
			}
		}
	}

	private float time;

	private RayCastCallback callback = (fixture, point, normal, fraction) -> {
		if (fixture.isSensor()) {
			return 1;
		}

		Entity entity = (Entity) fixture.getBody().getUserData();

		if ((entity == null && !fixture.getBody().isBullet()) || (entity instanceof Door && !((Door) entity).isOpen()) || entity instanceof Player) {
			if (fraction < closestFraction) {
				closestFraction = fraction;
				last = point;
			}

			return fraction;
		}

		return 1;
	};

	public static float shortAngleDist(float a0, float a1) {
		float max = (float) (Math.PI*2);
		float da = (a1 - a0) % max;
		return 2 * da % max - da;
	}

	public static float angleLerp(float a0, float a1, float t) {
		return a0 + shortAngleDist(a0,a1) * t;
	}

	protected float getAimX(float ex, float ey) {

		return (float) Math.cos(this.lastAngle) * (this.hole.x - this.origin.x + ex) + (float) Math.cos(this.lastAngle +
			(this.owner.isFlipped() ? -Math.PI / 2 : Math.PI / 2)) * (this.hole.y - this.origin.y + ey);
	}

	protected float getAimY(float ex, float ey) {


		return (float) Math.sin(this.lastAngle) * (this.hole.x - this.origin.x + ex) + (float) Math.sin(this.lastAngle +
			(this.owner.isFlipped() ? -Math.PI / 2 : Math.PI / 2)) * (this.hole.y - this.origin.y + ey);
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (!s) {
			s = true;

			this.tw = this.getSprite().getRegionWidth();
			this.th = this.getSprite().getRegionHeight();
		}

		Point aim = this.owner.getAim();
		float an = this.owner.getAngleTo(aim.x, aim.y);

		an = angleLerp(this.lastAngle, an, 0.15f);
		this.lastAngle = an;
		float a = (float) Math.toDegrees(this.lastAngle);

		this.renderAt(x + w / 2 + (flipped ? -7 : 7), y + h / 4 + this.owner.z, a + textureA, this.origin.x, this.origin.y,
			false, false, textureA == 0 ? this.sx : flipped ? -this.sx : this.sx, textureA != 0 ? this.sy : flipped ? -this.sy : this.sy);
		float r = 4;

		x = x + w / 2 + (flipped ? -7 : 7);
		y = y + h / 4 + this.owner.z;

		float xx = x + getAimX(0, 0);
		float yy = y + getAimY(0, 0);

		if (this.delay + 0.06f >= this.useTime) {
			Graphics.batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

			Graphics.shape.setColor(1, 0.5f, 0, 0.9f);

			Graphics.shape.circle(xx, yy, r);

			Graphics.shape.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			Graphics.batch.begin();
		}

		if (this.owner instanceof Player && ((Player) this.owner).hasRedLine) {
			float d = Display.GAME_WIDTH * 10;
			closestFraction = 1f;
			World.world.rayCast(callback, xx, yy, xx + (float) Math.cos(an) * d, yy + (float) Math.sin(an) * d);

			Graphics.batch.end();
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.setColor(1, 0, 0, 0.7f);

			Graphics.shape.line(xx, yy, last.x, last.y);
			Graphics.shape.rect(last.x - 2, last.y - 2, 4, 4);

			Graphics.shape.end();
			Graphics.batch.begin();
		}

		y += this.owner.h;
		x = this.owner.x + this.owner.w / 2;

		float dt = Gdx.graphics.getDeltaTime();

		if (this.chargeProgress > 0 && this.chargeProgress < 1f && this.chargeA < 1) {
			this.chargeA += (1 - this.chargeA) * dt * 5;
		} else if (this.chargeA > 0) {
			this.chargeA += -this.chargeA * dt * 5;

			if (this.chargeA <= 0) {
				this.chargeProgress = 0;
			}
		}

		if (this.chargeA > 0) {
			Graphics.startAlphaShape();
			Graphics.shape.setColor(1, 1, 1, this.chargeA);
			Graphics.shape.line(x - 8, y, x + 8, y);

			xx = this.chargeProgress * 16 - 8 + x;

			Graphics.shape.setColor(0, 0, 0, this.chargeA);
			Graphics.shape.rect(xx - 2, y - 2, 4, 4);
			Graphics.shape.setColor(1, 1, 1, this.chargeA);
			Graphics.shape.rect(xx - 1, y - 1, 2, 2);

			Graphics.endAlphaShape();
		}
	}

	private float chargeA = 0;

	@Override
	public void use() {
		if (this.ammoLeft <= 0) {
			return;
		}

		this.ammoLeft -= 1;

		super.use();
		this.owner.playSfx("gun_machinegun");
		Point aim = this.owner.getAim();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		Shell shell = new Shell();

		float x = this.owner.x + this.owner.w / 2f;
		float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

		shell.x = x;
		shell.y = y - 10;

		shell.vel = new Point(
			(float) -Math.cos(a) * 2f,
			1.5f
		);

		Dungeon.area.add(shell);

		this.owner.vel.x -= Math.cos(a) * 40f;
		this.owner.vel.y -= Math.sin(a) * 40f;

		Camera.push(a, 8f);
		Camera.shake(2);

		Tween.to(new Tween.Task(0.5f, 0.1f) {
			@Override
			public float getValue() {
				return sx;
			}

			@Override
			public void setValue(float value) {
				sx = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(1f, 0.2f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return sx;
					}

					@Override
					public void setValue(float value) {
						sx = value;
					}
				});
			}
		});

		Tween.to(new Tween.Task(1.4f, 0.1f) {
			@Override
			public float getValue() {
				return sy;
			}

			@Override
			public void setValue(float value) {
				sy = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(1f, 0.2f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return sy;
					}

					@Override
					public void setValue(float value) {
						sy = value;
					}
				});
			}
		});

		this.sendBullets();
	}

	protected void sendBullets() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.getAccuracy(), this.getAccuracy()))));
	}

	protected void sendBullet(float an) {
		sendBullet(an, 0, 0);
	}


	protected void sendBullet(float an, float xx, float yy) {
		sendBullet(an, xx, yy, new BulletProjectile());
	}

	protected void sendBullet(float an, float xx, float yy, BulletProjectile bullet) {
		float a = (float) Math.toDegrees(an);

		try {
			Bullet b = (this.ammo != null ? this.ammo.newInstance() : (Bullet) this.owner.getAmmo("bullet"));
			bullet.sprite = Graphics.getTexture("bullet-" + b.bulletName);

			float x = this.owner.x + this.owner.w / 2;
			float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

			bullet.x = x + this.getAimX(bullet.sprite.getRegionWidth() / 2, -bullet.sprite.getRegionHeight() / 2);
			bullet.y = y + this.getAimY(bullet.sprite.getRegionWidth() / 2, -bullet.sprite.getRegionHeight() / 2);
			bullet.damage = b.damage + rollDamage();
			bullet.crit = true;
			bullet.letter = b.bulletName;
			bullet.owner = this.owner;
			bullet.penetrates = this.penetrates;
			bullet.rotates = b.bulletName.equals("bill");

			if (b.bulletName.equals("snow")) {
				bullet.toApply = FreezeBuff.class;
				bullet.rotates = true;
			} else if (b.bulletName.equals("kotlin")) {
				bullet.rotates = true;
			}

			float s = this.vel * 60;

			bullet.vel = new Point(
				(float) Math.cos(an) * s, (float) Math.sin(an) * s
			);

			bullet.a = a;

			Dungeon.area.add(bullet);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
	}

	public float getAccuracy() {
		return Math.max(0, accuracy - (this.owner instanceof Player ? ((Player) this.owner).accuracy : 0));
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
}