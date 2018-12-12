package org.rexcellentgames.burningknight.physics;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.creature.fx.PoisonFx;
import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Shell;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class World {
	public static boolean DRAW_DEBUG = false;
	public static final float TIME_STEP = 1 / 100.0f;

	public static com.badlogic.gdx.physics.box2d.World world;
	private static Box2DDebugRenderer debug = new Box2DDebugRenderer();
	private static float accumulator;
	private static ArrayList<Body> toDestroy = new ArrayList<>();
	public static RayHandler lights;
	private static ArrayList<PointLight> lightPool = new ArrayList<>();

	public static PointLight newLight(int rays, Color color, int rad, float x, float y) {
		return newLight(rays, color, rad, x, y, false);
	}

	public static PointLight newLight(int rays, Color color, int rad, float x, float y, boolean fast) {
		if (lightPool.size() == 0) {
			return new PointLight(lights, rays, color, rad, x, y);
		}

		for (int i = 0; i < lightPool.size(); i++) {
			PointLight light = lightPool.get(i);

			if (light.getRayNum() == rays) {
				lightPool.remove(i);

				light.setColor(color);

				if (fast) {
					light.setDistance(rad);
				} else {
					Tween.to(new Tween.Task(rad, 0.2f) {
						@Override
						public float getValue() {
							return light.getDistance();
						}

						@Override
						public void setValue(float value) {
							light.setDistance(value);
						}
					});
				}

				light.setPosition(x, y);
				light.setActive(true);

				return light;
			}
		}

		return new PointLight(lights, rays, color, rad, x, y);
	}

	public static void removeLight(PointLight light) {
		if (light == null) {
			return;
		}

		Tween.to(new Tween.Task(0, 0.2f) {
			@Override
			public float getValue() {
				return light.getDistance();
			}

			@Override
			public void setValue(float value) {
				light.setDistance(value);
			}

			@Override
			public void onEnd() {
				lightPool.add(light);
				light.setDistance(0);
				light.setPosition(-1000, -1000);
				light.setActive(false);
			}
		});
	}

	/*
	 * Simple world logic
	 */

	public static void init() {
		if (world != null) {
			return;
		}

		Log.physics("Creating new world");
		world = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
		RayHandler.isDiffuse = true;

		lights = new RayHandler(world, Display.GAME_WIDTH, Display.GAME_HEIGHT);
		lights.setLightMapRendering(false);
		lights.setBlurNum(5);

		float v = 0.2f;
		lights.setAmbientLight(v, v, v, 1f);

		// categoryBits, groupIndex, maskBits
		Light.setGlobalContactFilter((short) 1, (short) -1, (short) 0x0003);
	}

	private static void setBits(FixtureDef fixture, Entity owner) {
		if (!(owner instanceof SolidProp) && (fixture.isSensor || owner instanceof HeartFx || owner instanceof Upgrade || owner instanceof Exit
			|| owner instanceof PoisonFx || owner instanceof ItemHolder || owner instanceof Item || owner instanceof Projectile || owner instanceof Shell)) {
			fixture.filter.categoryBits = 0x0002;
			fixture.filter.groupIndex = -1;
			fixture.filter.maskBits = -1;
		} else {
			fixture.filter.categoryBits = 0x0003;
			fixture.filter.groupIndex = 1;
			fixture.filter.maskBits = -1;
		}
	}

	public static void update(float dt) {
		float frameTime = Math.min(dt, 0.25f);
		accumulator += frameTime;

		while (accumulator >= TIME_STEP) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}

		if (!world.isLocked() && toDestroy.size() > 0) {
			Log.physics("Removing " + toDestroy.size() + " bodies");

			for (Body body : toDestroy) {
				world.destroyBody(body);
			}

			toDestroy.clear();
		}
	}

	public static void render() {
		if (DRAW_DEBUG) {
			Graphics.batch.end();
			debug.render(world, Camera.game.combined);
			Graphics.batch.begin();
		}
	}

	public static void destroy() {
		if (world == null) {
			return;
		}
		
		Log.physics("Destroying the world");
		world.dispose();
		world = null;

		if (lights == null) {
			return;
		}

		for (Light light : lightPool) {
			try {
				light.remove();
			} catch (RuntimeException e) {

			}
		}

		lights.dispose();
		lights = null;
	}

	/*
	 * Actual body creation
	 */

	public static Body createSimpleBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type) {
		return createSimpleBody(owner, x, y, w, h, type, false);
	}

	public static Body createSimpleBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type, boolean sensor) {
		return createSimpleBody(owner, x, y, w, h, type, sensor, 0f);
	}

	public static Body createSimpleBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type, boolean sensor, float den) {
		Log.physics("Creating body for " + (owner == null ? null : owner.getClass().getSimpleName()) + " with params (" + x + ", " + y + ", " + w + ", " + h + ") and sensor = " + sensor);

		if (world.isLocked()) {
			Log.physics("World is locked! Failed to create body");

			return null;
		}

		BodyDef def = new BodyDef();
		def.type = type;

		Body body = world.createBody(def);
		PolygonShape poly = new PolygonShape();

		poly.set(new Vector2[]{
			new Vector2(x, y), new Vector2(x + w, y),
			new Vector2(x, y + h), new Vector2(x + w, y + h)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = sensor;
		fixture.restitution = den;

		setBits(fixture, owner);

		body.createFixture(fixture);
		body.setUserData(owner);
		poly.dispose();

		return body;
	}

	public static Body checkLocked(Body body) {
		if (world.isLocked()) {
			throw new RuntimeException("World is locked!");
		}

		return body;
	}

	public static Body createSimpleCentredBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type) {
		return createSimpleCentredBody(owner, x, y, w, h, type, false);
	}


	public static Body createSimpleCentredBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type, boolean sensor) {
		return createSimpleCentredBody(owner, x, y, w, h, type, sensor, 0f);
	}

	public static Body createSimpleCentredBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type, boolean sensor, float den) {
		Log.physics("Creating centred body for " + owner.getClass().getSimpleName() + " with params (" + x + ", " + y + ", " + w + ", " + h + ") and sensor = " + sensor);

		if (world.isLocked()) {
			Log.physics("World is locked! Failed to create body");
			return null;
		}

		BodyDef def = new BodyDef();
		def.type = type;

		Body body = world.createBody(def);
		PolygonShape poly = new PolygonShape();

		poly.set(new Vector2[]{
			new Vector2(x - w / 2, y - h / 2), new Vector2(x + w / 2, y - h / 2),
			new Vector2(x - w / 2, y + h / 2), new Vector2(x + w / 2, y + h / 2)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.restitution = den;
		fixture.isSensor = sensor;

		setBits(fixture, owner);

		body.createFixture(fixture);
		body.setUserData(owner);
		poly.dispose();

		return body;
	}

	public static Body createCircleCentredBody(Entity owner, float x, float y, float r, BodyDef.BodyType type) {
		return createCircleCentredBody(owner, x, y, r, type, false);
	}

	public static Body createCircleBody(Entity owner, float x, float y, float r, BodyDef.BodyType type, boolean sensor) {
		return createCircleBody(owner, x, y, r, type, sensor, 0f);
	}

	public static Body createCircleBody(Entity owner, float x, float y, float r, BodyDef.BodyType type, boolean sensor, float restitution) {
		Log.physics("Creating circle centred body for " + owner.getClass().getSimpleName() + " with params (" + x + ", " + y + ", " + r + ") and sensor = " + sensor);

		if (world.isLocked()) {
			Log.physics("World is locked! Failed to create body");
			return null;
		}

		BodyDef def = new BodyDef();
		def.type = type;

		Body body = world.createBody(def);
		CircleShape poly = new CircleShape();
		poly.setPosition(new Vector2(r, r));

		poly.setRadius(r);

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = sensor;
		fixture.restitution = restitution;

		setBits(fixture, owner);

		body.createFixture(fixture);
		body.setUserData(owner);
		poly.dispose();

		return body;
	}

	public static Body createCircleCentredBody(Entity owner, float x, float y, float r, BodyDef.BodyType type, boolean sensor) {
		Log.physics("Creating circle centred body for " + owner.getClass().getSimpleName() + " with params (" + x + ", " + y + ", " + r + ") and sensor = " + sensor);

		if (world.isLocked()) {
			Log.physics("World is locked! Failed to create body");
			return null;
		}

		BodyDef def = new BodyDef();
		def.type = type;

		Body body = world.createBody(def);
		CircleShape poly = new CircleShape();

		poly.setRadius(r);

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = sensor;

		setBits(fixture, owner);

		body.createFixture(fixture);
		body.setUserData(owner);
		poly.dispose();

		return body;
	}

	/*
	 * Body removing
	 */

	public static Body removeBody(Body body) {
		if (body == null) {
			return null;
		}

		toDestroy.add(body);
		return null;
	}
}