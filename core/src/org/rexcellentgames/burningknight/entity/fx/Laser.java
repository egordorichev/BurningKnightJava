package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;

public class Laser extends Entity {
	public static TextureRegion start = Graphics.getTexture("laser-circ");
	public static TextureRegion startOverlay = Graphics.getTexture("laser-circ_over");
	public static TextureRegion mid = Graphics.getTexture("laser-mid");
	public static TextureRegion midOverlay = Graphics.getTexture("laser-mid_over");

	public static TextureRegion startHuge = Graphics.getTexture("laser-big_start");
	public static TextureRegion startOverlayHuge = Graphics.getTexture("laser-big_start_over");
	public static TextureRegion midHuge = Graphics.getTexture("laser-big_mid");
	public static TextureRegion midOverlayHuge = Graphics.getTexture("laser-big_mid_over");

	private Body body;
	public float a;
	private float al = 0.3f;
	public int damage;
	public boolean crit;
	public Color shade = new Color(1, 0, 0, 1);
	public Creature owner;
	public boolean huge;

	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 1;
	}

	@Override
	public void init() {
		super.init();

		Tween.to(new Tween.Task(1, 0.05f) {
			@Override
			public float getValue() {
				return al;
			}

			@Override
			public void setValue(float value) {
				al = value;
			}
		});
	}

	private boolean created;
	private float t;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (huge) {
			shade.g = (float) (Math.sin(this.t * 8) * 0.25f + 0.25f);
		}

		World.removeBody(body);

		float xx = x;
		float yy = y;
		float d = Display.GAME_WIDTH * 2;
		closestFraction = 1f;
		last = null;

		float an = (float) Math.toRadians(a);
		float x2 = xx + (float) Math.cos(an + Math.PI / 2) * d;
		float y2 = yy + (float) Math.sin(an + Math.PI / 2) * d;

		if (xx != x2 || yy != y2) {
			World.world.rayCast(callback, xx, yy, x2, y2);
		}

		float dx, dy;

		if (last != null) {
			dx = last.x - x;
			dy = last.y - y;
		} else {
			dx = x2 - x;
			dy = y2 - y;
		}

		// fixme: broken with chasms

		w = (float) Math.sqrt(dx * dx + dy * dy) + (huge ? 8 : 4);

		Log.physics("Creating centred body for laser");

		if (World.world.isLocked()) {
			Log.physics("World is locked! Failed to create body");
			return;
		}

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;

		body = World.world.createBody(def);
		PolygonShape poly = new PolygonShape();

		float x = 0f;
		float w = huge ? 12f : 6f;
		float h = this.w;
		float y = 0f;

		poly.set(new Vector2[] {
			new Vector2(x - w / 2, y - 4), new Vector2(x + w / 2, y - 4),
			new Vector2(x - w / 2, y + h - 4), new Vector2(x + w / 2, y + h - 4)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = true;

		fixture.filter.categoryBits = 0x0002;
		fixture.filter.groupIndex = -1;
		fixture.filter.maskBits = -1;

		body.createFixture(fixture);
		body.setUserData(this);
		poly.dispose();

		World.checkLocked(this.body).setTransform(this.x, this.y, an);
	}

	private static float closestFraction = 1.0f;
	private static Vector2 last;

	public void remove() {
		Tween.to(new Tween.Task(0, 0.3f) {
			@Override
			public float getValue() {
				return al;
			}

			@Override
			public void setValue(float value) {
				al = value;
			}

			@Override
			public void onEnd() {
				setDone(true);
				dead = true;
			}
		});
	}

	public boolean dead;

	private static RayCastCallback callback = (fixture, point, normal, fraction) -> {
		Object data = fixture.getBody().getUserData();

		if (!fixture.isSensor() && (data == null || ((data instanceof Door && !((Door) data).isOpen())))) {
			if (fraction < closestFraction) {
				closestFraction = fraction;
				last = point;
			}
		}

		return closestFraction;
	};

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	public void render() {
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		double a = Math.toRadians(this.a + 90);

		shade.a = this.al;
		Graphics.batch.setColor(shade);

		if (huge) {
			Graphics.render(startHuge, this.x, this.y, this.a, 16, 16, false, false);
			float s = (this.w - 8) / 32f;

			if (this.w > 16) {
				Graphics.render(midHuge, this.x, this.y, this.a, 16, 0, false, false, 1, s);
			}

			Graphics.render(startHuge, this.x + (float) Math.cos(a) * (w - 16), this.y + (float) Math.sin(a) * (w - 16), this.a, 16, 16, false, false);
			Graphics.batch.setColor(1, 1, 1, this.al);
			Graphics.render(startOverlayHuge, this.x, this.y, this.a, 16, 16, false, false);

			if (this.w > 16) {
				Graphics.render(midOverlayHuge, this.x, this.y, this.a, 16, 0, false, false, 1, s);
			}

			Graphics.render(startOverlayHuge, this.x + (float) Math.cos(a) * (w - 8), this.y + (float) Math.sin(a) * (w - 8), this.a, 16, 16, false, false);
		} else {
			Graphics.render(start, this.x, this.y, this.a, 8, 8, false, false);

			float s = (this.w - 4) / 16f;

			if (this.w > 8) {
				Graphics.render(mid, this.x, this.y, this.a, 8, 0, false, false, 1, s);
			}

			Graphics.render(start, this.x + (float) Math.cos(a) * (w - 4), this.y + (float) Math.sin(a) * (w - 4), this.a, 8, 8, false, false);
			Graphics.batch.setColor(1, 1, 1, this.al);
			Graphics.render(startOverlay, this.x, this.y, this.a, 8, 8, false, false);

			if (this.w > 8) {
				Graphics.render(midOverlay, this.x, this.y, this.a, 8, 0, false, false, 1, s);
			}

			Graphics.render(startOverlay, this.x + (float) Math.cos(a) * (w - 4), this.y + (float) Math.sin(a) * (w - 4), this.a, 8, 8, false, false);
		}

		Graphics.batch.setColor(1, 1, 1, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Mob) {
			HpFx fx = ((Mob) entity).modifyHp(-this.damage, this.owner, true);

			if (fx != null) {
				fx.crit = this.crit;
			}
		}
	}
}