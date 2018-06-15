package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Bullet;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Shell;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.Turret;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Gun extends WeaponBase {
	protected float accuracy = 10f;
	protected float sx = 1f;
	protected float sy = 1f;
	protected float vel = 6f;
	protected Class<? extends Bullet> ammo;
	protected float textureA;
	protected boolean penetrates;
	protected float tw;
	protected float th;
	protected int ox = 3;
	protected boolean s;

	{
		identified = true;
		auto = true;
		useTime = 0.2f;
	}

	private Vector2 last = new Point();
	private float lastAngle;

	private float closestFraction = 1.0f;

	private RayCastCallback callback = (fixture, point, normal, fraction) -> {
		if(fixture.isSensor()) {
			return 1;
		}

		Entity entity = (Entity) fixture.getBody().getUserData();

		if (entity == null || (entity instanceof Door && !((Door) entity).isOpen()) || (entity instanceof SolidProp && !(entity instanceof Turret)) || entity instanceof Creature) {

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

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (!s) {
			s = true;

			this.tw = this.getSprite().getRegionWidth();
			this.th = this.getSprite().getRegionHeight();
		}

		TextureRegion sprite = this.getSprite();
		Point aim = this.owner.getAim();

		float an = this.owner.getAngleTo(aim.x, aim.y);
		an = angleLerp(this.lastAngle, an, 0.15f);
		this.lastAngle = an;
		float a = (float) Math.toDegrees(this.lastAngle);

		this.renderAt(x + w / 2 + (flipped ? -7 : 7), y + h / 4 + this.owner.z, a + textureA, this.ox, sprite.getRegionHeight() / 2,
			false, false, textureA == 0 ? this.sx : flipped ? -this.sx : this.sx, textureA != 0 ? this.sy : flipped ? -this.sy : this.sy);
		float r = 6;

		x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
		y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

		float px = this.tw;

		float xx = (float) (x + px * Math.cos(an) - this.ox);
		float yy = (float) (y + px * Math.sin(an));

		if (this.delay + 0.09f >= this.useTime) {
			Graphics.batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

			Graphics.shape.setColor(1, 0.5f, 0, 0.7f);

			Graphics.shape.circle(xx, yy, r);

			Graphics.shape.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			Graphics.batch.begin();
		}

		if (this.owner instanceof Player) {
			float d = Display.GAME_WIDTH;
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
	}

	@Override
	public void use() {
		super.use();
		this.owner.playSfx("gun_machinegun");
		Point aim = this.owner.getAim();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		Shell shell = new Shell();

		float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
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

		this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy))));
	}

	protected void sendBullet(float an) {
		sendBullet(an, 0, 0);
	}


	protected void sendBullet(float an, float xx, float yy) {
		sendBullet(an, xx, yy, new BulletEntity());
	}

	protected void sendBullet(float an, float xx, float yy, BulletEntity bullet) {
		TextureRegion sprite = this.getSprite();
		float a = (float) Math.toDegrees(an);

		try {
			Bullet b = (this.ammo != null ? this.ammo.newInstance() : (Bullet) this.owner.getAmmo("bullet"));
			bullet.sprite = Graphics.getTexture("bullet (" + b.bulletName + ")");

			float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
			float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

			float px = this.tw;

			bullet.x = (float) (x + px * Math.cos(an) - this.ox);
			bullet.y = (float) (y + px * Math.sin(an));
			bullet.damage = b.damage + rollDamage();
			bullet.crit = true;
			bullet.letter = b.bulletName;
			bullet.owner = this.owner;
			bullet.penetrates = this.penetrates;

			float s = this.vel * 60f;

			bullet.vel = new Point(
				(float) Math.cos(an) * s, (float) Math.sin(an) * s
			);

			bullet.a = a;

			Dungeon.area.add(bullet);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
	}
}