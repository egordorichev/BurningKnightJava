package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Bullet;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Shell;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class Gun extends WeaponBase {
	protected float accuracy = 4f;
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
	protected int ammoMax = 20;
	protected int ammoLeft = 20;
	protected float chargeProgress;
	protected float reloadRate = 1;

	{
		sprite = "item-gun_a";
		damage = 4;
		useTime = 0.8f;
	}

	protected void setStats() {

	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.ammoLeft = reader.readInt32();

		setStats();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt32(this.ammoLeft);
	}

	@Override
	public void upgrade() {
		super.upgrade();
		setStats();
	}

	@Override
	public int getValue() {
		return this.ammoLeft;
	}

	public int getAmmoLeft() {
		return ammoLeft;
	}

	{
		useTime = 0.2f;
	}

	private static Vector2 last = new Vector2();
	protected float lastAngle;

	private static float closestFraction = 1.0f;
	
	public Gun() {
	  setStats();
  }

	@Override
	public boolean canBeUsed() {
		return super.canBeUsed() && ammoLeft >= 0;
	}

	private boolean pressed;
	private boolean shown;

	public boolean isReloading() {
		return pressed;
	}

	@Override
	public void updateInHands(float dt) {
		super.updateInHands(dt);

		if (ammoLeft == 0 && this.chargeProgress == 0 && (this.owner instanceof Mob)) {
			pressed = true;
		}

		if (ammoLeft == 0 && Dungeon.depth == -3 && !shown) {
			shown = true;
			Ui.ui.addControl("[white]" + Input.instance.getMapping("interact") + " [gray]" + Locale.get("reload"));
		}

		if (ammoLeft < ammoMax && (pressed || Input.instance.wasPressed("interact"))) {
			if (!pressed) {
				Ui.ui.hideControlsFast();
				this.owner.playSfx("reload_1");
			}

			pressed = true;

			if (this.chargeProgress == 0 || this.time == 0) {
				this.time = this.owner.getStat("reload_time");
			}

			this.chargeProgress += dt * this.time / 3f * reloadRate;

			if (this.chargeProgress >= 1f) {
				pressed = false;
				this.ammoLeft = (int) (this.ammoMax * this.owner.getStat("ammo_capacity"));
				this.onAmmoAdded();
				chargeProgress = 0;
			}
		}
	}

	protected void onAmmoAdded() {

	}

	private float time;

	private static RayCastCallback callback = (fixture, point, normal, fraction) -> {
		Object data = fixture.getBody().getUserData();

		if (data == null || (data instanceof Door && !((Door) data).isOpen()) || data instanceof SolidProp || data instanceof RollingSpike || data instanceof Creature) {
			if (fraction < closestFraction) {
				closestFraction = fraction;
				last.x = point.x;
				last.y = point.y;
			}
		}

		return closestFraction;
	};

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.owner != null) {
			this.delay = Math.max(0, this.delay - dt * this.owner.getStat("gun_use_time"));

			float cp = this.owner.getStat("ammo_capacity");

			if (this.ammoLeft > this.ammoMax * cp) {
				this.ammoLeft = (int) (this.ammoMax * cp);
			}
		}
	}

	public static float shortAngleDist(float a0, float a1) {
		float max = (float) (Math.PI * 2);
		float da = (a1 - a0) % max;
		return 2 * da % max - da;
	}

	public static float angleLerp(float a0, float a1, float t, boolean freezed) {
		return Dungeon.game.getState().isPaused() || (freezed) ? a0 : a0 + shortAngleDist(a0, a1) * (t * 60 * Gdx.graphics.getDeltaTime());
	}

	protected float getAimX(float ex, float ey) {
		return (float) Math.cos(this.lastAngle) * (this.hole.x - this.origin.x + ex) + (float) Math.cos(this.lastAngle +
			(flipped ? -Math.PI / 2 : Math.PI / 2)) * (this.hole.y - this.origin.y + ey);
	}

	protected float getAimY(float ex, float ey) {
		return (float) Math.sin(this.lastAngle) * (this.hole.x - this.origin.x + ex) + (float) Math.sin(this.lastAngle +
			(flipped ? -Math.PI / 2 : Math.PI / 2)) * (this.hole.y - this.origin.y + ey);
	}
	
	protected boolean flipped;
	protected boolean lastFlip;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (!s) {
			s = true;

			this.tw = this.getSprite().getRegionWidth();
			this.th = this.getSprite().getRegionHeight();
		}

		Point aim = this.owner.getAim();
		flipped = Dungeon.game.getState().isPaused() ? this.lastFlip : aim.x < this.owner.x + this.owner.w / 2;
		this.lastFlip = this.flipped;

		this.flipped = flipped;
		float an = this.owner.getAngleTo(aim.x, aim.y);

		an = angleLerp(this.lastAngle, an, 0.15f, this.owner != null && this.owner.freezed);
		this.lastAngle = an;
		float a = (float) Math.toDegrees(this.lastAngle);

		this.renderAt(x + w / 2 + (flipped ? -7 : 7), y + h / 4 + this.owner.z, a + textureA, this.origin.x + this.mod, this.origin.y,
			false, false, textureA == 0 ? this.sx : flipped ? -this.sx : this.sx, textureA != 0 ? this.sy : flipped ? -this.sy : this.sy);

		x = x + w / 2 + (flipped ? -7 : 7);
		y = y + h / 4 + this.owner.z;

		float xx = x + getAimX(0, 0);
		float yy = y + getAimY(0, 0);

		if (this.owner instanceof Player && ((Player) this.owner).hasRedLine) {
			float d = Display.GAME_WIDTH * 2;
			closestFraction = 1f;
			last.x = -1;

			float x2 = xx + (float) Math.cos(an) * d;
			float y2 = yy + (float) Math.sin(an) * d;

			if (xx != x2 || yy != y2) {
				World.world.rayCast(callback, xx, yy, x2, y2);
			}

			float tx, ty;

			if (last.x != -1) {
				tx = last.x;
				ty = last.y;
			} else {
				tx = x2;
				ty = y2;
			}

			Graphics.startAlphaShape();
			Graphics.shape.setProjectionMatrix(Camera.game.combined);

			Graphics.shape.setColor(1, 0, 0, 0.3f);
			Graphics.shape.rectLine(xx, yy, tx, ty, 3);
			Graphics.shape.rect(tx - 2.5f, ty - 2.5f, 5, 5);
			Graphics.shape.setColor(1, 0, 0, 0.7f);
			Graphics.shape.rectLine(xx, yy, tx, ty, 1);
			Graphics.shape.rect(tx - 1.5f, ty - 1.5f, 3, 3);

			Graphics.endAlphaShape();

		}

		float dt = Gdx.graphics.getDeltaTime();

		if (this.chargeProgress > 0 && this.chargeProgress < 1f && this.chargeA < 1) {
			this.chargeA += (1 - this.chargeA) * dt * 5;
			this.back = false;
		} else if (this.chargeA > 0) {
			if (!this.back) {
				if (this.owner.getStat("fire_on_reload") > 0) {
					this.delay = 0;
					this.ammoLeft ++;
					this.use();
				}
			}

			this.chargeProgress = 0;
			this.back = true;
			this.chargeA += -this.chargeA * dt * 5;
		}
	}

	public void renderReload() {
		if (this.chargeA > 0) {
			float x = this.owner.x + this.owner.w / 2 ;
			float y = this.owner.y + this.owner.h;

			Graphics.startAlphaShape();

			Graphics.shape.setColor(0, 0, 0, chargeA);
			Graphics.shape.rect(x - 9, y - 1, 18, 3);
			Graphics.shape.setColor(1, 1, 1, chargeA);
			Graphics.shape.rect(x - 8, y, 16, 1);

			float xx = (back ? 16 : this.chargeProgress * 16) - 8 + x;

			Graphics.shape.setColor(0, 0, 0, this.chargeA);
			Graphics.shape.rect(xx - 2, y - 2, 5, 5);
			Graphics.shape.setColor(1, 1, 1, this.chargeA);
			Graphics.shape.rect(xx - 1, y - 1, 3, 3);

			Graphics.endAlphaShape();
		}
	}

	private boolean back = false;
	private float chargeA = 0;
	private float mod;
	protected int usedTime;

	@Override
	public void use() {
		if (this.delay > 0) {
			return;
		}

		if (this.ammoLeft == 0) {
			this.owner.playSfx("item_nocash");
		}

		if (this.ammoLeft <= 0 || this.chargeProgress != 0) {
			if (this.chargeProgress == 0 && (this.owner instanceof Mob)) {
				pressed = true;
			}

			return;
		}

		boolean rng = Random.chance(this.owner.getStat("restore_ammo_chance") * 100);

		if (rng) {
			ammoLeft += 1f;
		} else if (!Random.chance(this.owner.getStat("ammo_save_chance") * 100)) {
			this.ammoLeft -= 1;
		}

		Tween.to(new Tween.Task(6, 0.05f) {
			@Override
			public float getValue() {
				return mod;
			}

			@Override
			public void setValue(float value) {
				mod = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return mod;
					}

					@Override
					public void setValue(float value) {
						mod = value;
					}
				});
			}
		});

		float t = this.useTime;
		this.useTime = this.getUseTimeGun();

		super.use();

		this.useTime = t;
		this.usedTime += 1;

		getSprite();

		this.owner.playSfx(getSfx());

		Point aim = this.owner.getAim();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		Shell shell = new Shell();

		float x = this.owner.x + this.owner.w / 2f;
		float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

		shell.x = x;
		shell.y = y - 10;

		shell.vel = new Point(
			(float) -Math.cos(a) * 1f,
			1.5f
		);

		Dungeon.area.add(shell);

		this.owner.knockback.x -= Math.cos(a) * 90f;
		this.owner.knockback.y -= Math.sin(a) * 90f;

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

		if (!rng) {
			this.sendBullets();
		}
	}

	protected float getUseTimeGun() {
		return useTime;
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

			if (!b.bulletName.startsWith("bullet-")) {
			  b.bulletName = "bullet-" + b.bulletName;
			}

      bullet.sprite = Graphics.getTexture(b.bulletName);
			
			float x = this.owner.x + this.owner.w / 2 + (flipped ? -7 : 7);
			float y = this.owner.y + this.owner.h / 4 + this.owner.z;

			bullet.x = (float) (x + this.getAimX(xx, yy));
			bullet.y = (float) (y + this.getAimY(xx, yy));
			bullet.damage = b.damage + rollDamage();
			bullet.crit = true;
			bullet.letter = b.bulletName;
			bullet.owner = this.owner;
			bullet.bad = this.owner instanceof Mob;
			bullet.penetrates = this.penetrates;
			bullet.gun = this;
			bullet.rotates = b.bulletName.equals("bill");

			this.modifyBullet(bullet);

			if (b.bulletName.equals("snow")) {
				bullet.toApply = FreezeBuff.class;
				bullet.rotates = true;
			} else if (b.bulletName.equals("kotlin")) {
				bullet.rotates = true;
			}

			float s = this.vel * 60f;

			bullet.velocity = new Point(
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

	public void setAmmoLeft(int i) {
		this.ammoLeft = i;
	}

	protected void modifyBullet(BulletProjectile bullet) {

	}

	protected String getSfx() {
		return "gun_6";
	}
}