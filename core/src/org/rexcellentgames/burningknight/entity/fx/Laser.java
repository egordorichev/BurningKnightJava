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
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Laser extends Entity {
	public static TextureRegion start = Graphics.getTexture("laser-circ");
	public static TextureRegion startOverlay = Graphics.getTexture("laser-circ_over");
	public static TextureRegion mid = Graphics.getTexture("laser-mid");
	public static TextureRegion midOverlay = Graphics.getTexture("laser-mid_over");

	public static TextureRegion startHuge = Graphics.getTexture("laser-big_start");
	public static TextureRegion startOverlayHuge = Graphics.getTexture("laser-big_start_over");
	public static TextureRegion midHuge = Graphics.getTexture("laser-big_mid");
	public static TextureRegion midOverlayHuge = Graphics.getTexture("laser-big_mid_over");

	protected Body body;
	public float a;
	public float al = 0.3f;
	public int damage;
	public boolean crit;
	public boolean bad;
	public Color shade = new Color(1, 0, 0, 1);
	public Creature owner;
	public boolean huge;
	public boolean fake;

	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 1;
		damage = 1;
	}

	@Override
	public void init() {
		super.init();

		Tween.to(new Tween.Task(1, fake ? 0.4f : 0.1f) {
			@Override
			public float getValue() {
				return al;
			}

			@Override
			public void setValue(float value) {
				al = value;
			}
		});

		recalc();
	}

	private float t;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (huge) {
			shade.g = (float) (Math.sin(this.t * 8) * 0.25f + 0.25f);
		}

		if (!fake) {
			for (Creature creature : colliding) {
				HpFx fx = creature.modifyHp(-this.damage, this.owner, true);

				if (fx != null) {
					fx.crit = this.crit;
				}
			}
		}
	}

	public void recalc() {
		if (removing) {
			return;
		}

		World.removeBody(body);

		float xx = x;
		float yy = y;
		float d = Display.GAME_WIDTH * 2;
		closestFraction = 1f;
		last.x = -1;

		float an = (float) Math.toRadians(a);
		float x2 = xx + (float) Math.cos(an + Math.PI / 2) * d;
		float y2 = yy + (float) Math.sin(an + Math.PI / 2) * d;

		if (xx != x2 || yy != y2) {
			World.world.rayCast(callback, xx, yy, x2, y2);
		}

		float dx;
		float dy;

		if (last.x != -1) {
			dx = last.x - x;
			dy = last.y - y;
		} else {
			dx = x2 - x;
			dy = y2 - y;
		}

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

	public boolean removing;
	private static float closestFraction = 1.0f;
	private static Vector2 last = new Vector2();

	public void remove() {
		removing = true;
		body = World.removeBody(body);

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

	private static RayCastCallback callback = new RayCastCallback() {
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			Object data = fixture.getBody().getUserData();

			if (data == null || (data instanceof Door && !((Door) data).isOpen()) || data instanceof SolidProp || data instanceof RollingSpike) {
				if (fraction < closestFraction) {
					closestFraction = fraction;
					last.x = point.x;
					last.y = point.y;
				}
			}

			return closestFraction;
		}
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

		doRender((float) a);

		if (fake && this.al != 1 && !this.removing) {
			doRender((float) (a - Math.PI * 0.3f * (1 - this.al)));
			doRender((float) (a + Math.PI * 0.3f * (1 - this.al)));
		}

		Graphics.batch.setColor(1, 1, 1, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void doRender(float a) {
		float an = (float) Math.toDegrees(a) - 90;
		Graphics.batch.setColor(shade);

		if (huge) {
			Graphics.render(startHuge, this.x, this.y, an, 16, 16, false, false);
			float s = (this.w - 8) / 32f;

			if (this.w > 16) {
				Graphics.render(midHuge, this.x, this.y, an, 16, 0, false, false, 1, s);
			}

			Graphics.render(startHuge, this.x + (float) Math.cos(a) * (w - 8), this.y + (float) Math.sin(a) * (w - 8), an, 16, 16, false, false);
			Graphics.batch.setColor(1, 1, 1, al);
			Graphics.render(startOverlayHuge, this.x, this.y, an, 16, 16, false, false);

			if (this.w > 16) {
				Graphics.render(midOverlayHuge, this.x, this.y, an, 16, 0, false, false, 1, s);
			}

			Graphics.render(startOverlayHuge, this.x + (float) Math.cos(a) * (w - 8), this.y + (float) Math.sin(a) * (w - 8), an, 16, 16, false, false);
		} else {
			Graphics.render(start, this.x, this.y, an, 8, 8, false, false);

			float s = (this.w - 4) / 16f;

			if (this.w > 8) {
				Graphics.render(mid, this.x, this.y, an, 8, 0, false, false, 1, s);
			}

			Graphics.render(start, this.x + (float) Math.cos(a) * (w - 4), this.y + (float) Math.sin(a) * (w - 4), an, 8, 8, false, false);
			Graphics.batch.setColor(1, 1, 1, al);
			Graphics.render(startOverlay, this.x, this.y, an, 8, 8, false, false);

			if (this.w > 8) {
				Graphics.render(midOverlay, this.x, this.y, an, 8, 0, false, false, 1, s);
			}

			Graphics.render(startOverlay, this.x + (float) Math.cos(a) * (w - 4), this.y + (float) Math.sin(a) * (w - 4), an, 8, 8, false, false);
		}
	}

	public void renderFrom(Point from, Point to) {
		Graphics.shape.setColor(shade.r, shade.g, shade.b, 0.5f);
		Graphics.shape.rectLine(from.x, from.y, to.x, to.y, 3);
		Graphics.shape.setColor(1, 1, 1, 1);
		Graphics.shape.line(from.x, from.y, to.x, to.y);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Creature && entity != this.owner && (entity instanceof Mob) != this.bad) {
			this.colliding.add((Creature) entity);

			if (!fake) {
				HpFx fx = ((Creature) entity).modifyHp(-this.damage, this.owner, true);

				if (fx != null) {
					fx.crit = this.crit;
				}
			}
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		super.onCollisionEnd(entity);

		if (entity instanceof Creature) {
			this.colliding.remove(entity);
		}
	}

	private ArrayList<Creature> colliding = new ArrayList<>();
}