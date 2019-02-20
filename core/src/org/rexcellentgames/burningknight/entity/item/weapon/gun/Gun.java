package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Archeologist;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.NanoBullet;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.SimpleBullet;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class Gun extends WeaponBase {
	private static Vector2 last = new Vector2();
	private static float closestFraction = 1.0f;
	private static RayCastCallback callback = (fixture, point, normal, fraction) -> {
		Object data = fixture.getBody().getUserData();

		if (data == null || (data instanceof Door && !((Door) data).isOpen())) {
			if (fraction < closestFraction) {
				closestFraction = fraction;
				last.x = point.x;
				last.y = point.y;
			}
		}

		return closestFraction;
	};
	public int ammoMax = 12;
	public boolean showRedLine;
	protected float accuracy = -3f;
	protected float sx = 1f;
	protected float sy = 1f;
	protected float vel = 6f;
	protected float textureA;
	protected boolean penetrates;
	protected float tw;
	protected float th;
	protected boolean s;
	protected Point origin = new Point(3, 1);
	protected Point hole = new Point(13, 6);
	protected int ammoLeft = 12;
	protected float chargeProgress;
	protected float reloadRate = 1;
	protected boolean down;
	protected float lastAngle;
	protected boolean flipped;
	protected boolean lastFlip;
	protected int usedTime;
	protected String bulletSprite;
	private boolean pressed;
	private boolean shown;
	private float time;
	private boolean fired;
	private boolean back = false;
	private float chargeA = 0;
	private float mod;

	{
		sprite = "item-gun_a";
		damage = 4;
		useTime = 0.8f;
	}

	{
		useTime = 0.2f;
	}

	public int getAmmoLeft() {
		return ammoLeft;
	}

	public void setAmmoLeft(int i) {
		this.ammoLeft = i;
	}

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
			// Ui.ui.addControl("[white]" + Input.instance.getMapping("interact") + " [gray]" + Locale.get("reload"));
		}

		if (ammoLeft < ammoMax && pressed) {
			if (this.chargeProgress == 0 || this.time == 0) {
				this.time = 1;
			}

			this.chargeProgress += dt * this.time * reloadRate;

			if (this.chargeProgress >= 1f) {
				pressed = false;
				this.ammoLeft = (this.ammoMax);
				this.onAmmoAdded();
				chargeProgress = 0;
			}
		} else if (ammoLeft < ammoMax && this.owner instanceof Player && ((Player) this.owner).stopT > 0.15f && !down) {
			this.chargeProgress += dt * reloadRate * ammoMax;

			if (this.chargeProgress >= 1f) {
				ammoLeft ++;
				this.onAmmoAdded();
				chargeProgress = 0;

				if (this.ammoLeft == this.ammoMax && this.owner instanceof Player) {
					this.owner.playSfx("magnet_start");
				}
			}
		}
	}
	
	@Override
	public int getValue() {
		return this.ammoLeft;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped, boolean back) {
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

		if (back) {
			flipped = !flipped;
		}

		this.renderAt(x + w / 2 + (flipped ? -7 : 7), y + h / 4 + this.owner.z, back ? (flipped ? 45 + 90 : 45 ) : (a + textureA), this.origin.x + this.mod, this.origin.y,
			false, false, textureA == 0 ? this.sx : flipped ? -this.sx : this.sx, textureA != 0 ? this.sy : (flipped) ? -this.sy : this.sy);

		x = x + w / 2 + (flipped ? -7 : 7);
		y = y + h / 4 + this.owner.z;

		float xx = x + getAimX(0, 0);
		float yy = y + getAimY(0, 0);

		if (!back && (this.showRedLine || (this.owner instanceof Player && ((Player) this.owner).hasRedLine))) {
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

		if (this.chargeProgress > 0 && this.chargeProgress < 1f && this.chargeA < 1 && (this.owner instanceof Mob || ((Player) this.owner).stopT > 0.1f)) {
			this.chargeA += (1 - this.chargeA) * dt * 5;
			this.back = false;
		} else if (this.chargeA > 0) {
			this.chargeProgress = 0;
			this.back = (this.owner instanceof Mob || (this.ammoLeft == this.ammoMax));
			this.chargeA += -this.chargeA * dt * 5;
		}

		if (this.chargeA <= 0.05f) {
			if (!fired) {
				fired = true;
			}
		} else {
			fired = false;
		}
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

	public static float shortAngleDist(float a0, float a1) {
		float max = (float) (Math.PI * 2);
		float da = (a1 - a0) % max;
		return 2 * da % max - da;
	}

	@Override
	public void use() {
		if (this.delay > 0) {
			return;
		}

		if (!(this.owner instanceof Archeologist)) {
			if (this.ammoLeft <= 0 || this.chargeProgress != 0) {
				if (this.chargeProgress == 0 && (this.owner instanceof Mob)) {
					pressed = true;
				}
			}

			if (this.ammoLeft <= 0) {
				if (this.owner instanceof Player) {
					this.owner.playSfx("no_ammo");
				}

				return;
			}
		}

		this.ammoLeft -= 1;

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

		// fixme: use getSfx*(
		this.owner.playSfx("gun_machinegun");

		Point aim = this.owner.getAim();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		if (Settings.quality > 0) {
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
		}

		this.owner.knockback.x -= Math.cos(a) * 30f;
		this.owner.knockback.y -= Math.sin(a) * 30f;

		if (this.owner instanceof Player) {
			Camera.push(a, 16f);
		}

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

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt32(this.ammoLeft);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.ammoLeft = reader.readInt32();
	}

	@Override
	public boolean canBeUsed() {
		return super.canBeUsed() && ammoLeft >= 0;
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

	public float getAccuracy() {
		return Math.max(0, accuracy - (this.owner instanceof Player ? ((Player) this.owner).accuracy : 0));
	}

	protected BulletProjectile getBullet() {
		return this.owner == null || this.owner instanceof Mob ? new NanoBullet() : new SimpleBullet();
	}

	protected void sendBullet(float an, float xx, float yy) {
		sendBullet(an, xx, yy, getBullet());
	}

	protected void sendBullet(float an, float xx, float yy, BulletProjectile bullet) {
		float a = (float) Math.toDegrees(an);

		float x = this.owner.x + this.owner.w / 2 + (flipped ? -7 : 7);
		float y = this.owner.y + this.owner.h / 4 + this.owner.z;

		bullet.x = (x + this.getAimX(xx, yy));
		bullet.y = (y + this.getAimY(xx, yy));
		bullet.damage = rollDamage();
		bullet.owner = this.owner;
		bullet.bad = this.owner instanceof Mob;
		bullet.penetrates = this.penetrates;
		bullet.gun = this;

		this.modifyBullet(bullet);

		float s = this.vel * 60f;

		bullet.velocity = new Point(
			(float) Math.cos(an) * s, (float) Math.sin(an) * s
		);

		bullet.a = a;

		Dungeon.area.add(bullet);
	}

	protected void modifyBullet(BulletProjectile bullet) {

	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	protected void onAmmoAdded() {

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

			float xx = (back ? 16 : (this.owner instanceof Mob ? this.chargeProgress : (((float) (this.ammoLeft) / this.ammoMax) + this.chargeProgress / 16f)) * 16) - 8 + x;

			Graphics.shape.setColor(0, 0, 0, this.chargeA);
			Graphics.shape.rect(xx - 2, y - 2, 5, 5);
			Graphics.shape.setColor(1, 1, 1, this.chargeA);
			Graphics.shape.rect(xx - 1, y - 1, 3, 3);

			Graphics.endAlphaShape();
		}
	}

	protected String getSfx() {
		return "gun_6";
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.owner != null) {
			this.delay = Math.max(0, this.delay - dt);

			if (this.ammoLeft > this.ammoMax) {
				this.ammoLeft = (int) (this.ammoMax);
			}
		}
	}
}