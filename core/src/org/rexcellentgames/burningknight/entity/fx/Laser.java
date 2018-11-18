package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Laser extends Entity {
	public static TextureRegion start = Graphics.getTexture("laser-start");
	public static TextureRegion startOverlay = Graphics.getTexture("laser-start_over");
	public static TextureRegion end = Graphics.getTexture("laser-end");
	public static TextureRegion endOverlay = Graphics.getTexture("laser-end_over");
	public static TextureRegion mid = Graphics.getTexture("laser-mid");
	public static TextureRegion midOverlay = Graphics.getTexture("laser-mid_over");
	public static TextureRegion circ = Graphics.getTexture("laser-circleShape");
	public static TextureRegion circOverlay = Graphics.getTexture("laser-circ_over");

	private Body body;
	public float a;
	private float al = 0.3f;
	public int damage;
	public boolean crit;
	public Color shade = new Color(1, 0, 0, 1);
	public Color color = new Color(1, 0f, 0f, 1);
	public Creature owner;

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

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.95f) {
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
					}
				});
			}
		});
	}

	private boolean created;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!created) {
			created = true;

			float d = Display.GAME_WIDTH * 1.5f;
			closestFraction = 1f;
			last = null;

			float an = (float) Math.toRadians(a);
			float x2 = x + (float) Math.cos(an) * d;
			float y2 = y + (float) Math.sin(an) * d;

			World.world.rayCast(callback, x, y, x2, y2);

			float dx;
			float dy;

			if (last != null) {
				dx = last.x - x;
				dy = last.y - y;
			} else {
				dx = x2;
				dy = y2;
			}

			w = (float) Math.sqrt(dx * dx + dy * dy);

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
			float w = 6f;
			float h = this.w - 8;
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
	}

	private static float closestFraction = 1.0f;
	private static Vector2 last;

	private static RayCastCallback callback = (fixture, point, normal, fraction) -> {
		if (fixture.isSensor()) {
			return 1;
		}

		Entity entity = (Entity) fixture.getBody().getUserData();

		if ((entity == null && !fixture.getBody().isBullet()) || (entity instanceof Door && !((Door) entity).isOpen())) {
			if (fraction < closestFraction) {
				closestFraction = fraction;
				last = point;
			}

			return fraction;
		}

		return 1;
	};

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	public void render() {
		start.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		double a = Math.toRadians(this.a + 90);

		shade.a = this.al;
		color.a = this.al;
		Graphics.batch.setColor(shade);

		Graphics.render(start, this.x, this.y, this.a, 8, 8, false, false);

		if (this.w > 32) {
			// Graphics.render(mid, this.x + (float) Math.cos(a) * 40, this.y + (float) Math.sin(a) * 40, this.a, 8, 8, false, false, 1, (w - 32) / 16);
		}

		Graphics.render(end, this.x + (float) Math.cos(a) * (w - 16), this.y + (float) Math.sin(a) * (w - 16), this.a, 8, 8, false, false);
		Graphics.batch.setColor(1, 1, 1, this.al);
		Graphics.render(startOverlay, this.x, this.y, this.a, 8, 8, false, false);

		if (this.w > 32) {
			// Graphics.render(midOverlay, this.x + (float) Math.cos(a) * 40, this.y + (float) Math.sin(a) * 40, this.a, 8, 8, false, false, 1, (w - 32) / 16);
		}

		Graphics.render(endOverlay, this.x + (float) Math.cos(a) * (w - 16), this.y + (float) Math.sin(a) * (w - 16), this.a, 8, 8, false, false);


		Graphics.batch.setColor(1, 1, 1, 1);
		start.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void renderFrom(Point from, Point to) {
		float dx = to.x - from.x;
		float dy = to.y - from.y;
		double d = Math.sqrt(dx * dx + dy * dy);
		double a = Math.atan2(dy, dx);

		start.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		// int v = (int) (Math.ceil(d / 16) - 1);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		float an = (float) Math.toDegrees(a - Math.PI / 2);

		shade.a = this.al;
		color.a = this.al;

		float s = (float) (d / 16f);

		Graphics.batch.setColor(shade);
		Graphics.render(mid, from.x, from.y, an, 8, 8, false, false, 1, s);
		Graphics.render(circ, to.x - (float) Math.cos(a) * 8, to.y - (float) Math.sin(a) * 8, an, 8, 8, false, false);
		Graphics.batch.setColor(color);
		Graphics.render(midOverlay, from.x, from.y, an, 8, 8, false, false, 1, s);
		Graphics.render(circOverlay, to.x - (float) Math.cos(a) * 8, to.y - (float) Math.sin(a) * 8, an, 8, 8, false, false);

		Graphics.batch.setColor(1, 1, 1, this.al);
		Graphics.render(midOverlay, from.x, from.y, an, 8, 8, false, false);
		Graphics.render(circOverlay, to.x - (float) Math.cos(a) * 8, to.y - (float) Math.sin(a) * 8, an, 8, 8, false, false);

		Graphics.batch.setColor(1, 1, 1, 1);
		start.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
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