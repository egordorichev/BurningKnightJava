package org.rexellentgames.dungeon.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Log;

public class World {
	public static boolean DRAW_DEBUG = false;
	public static final float TIME_STEP = 1 / 60.0f;

	public static com.badlogic.gdx.physics.box2d.World world;
	private static Box2DDebugRenderer debug = new Box2DDebugRenderer();
	private static float accumulator;

	/*
	 * Simple world logic
	 */

	public static void init() {
		if (world != null) {
			return;
		}

		Log.physics("Creating new world");
		world = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
	}

	public static void update(float dt) {
		float frameTime = Math.min(dt, 0.25f);
		accumulator += frameTime;

		while (accumulator >= TIME_STEP) {
			Dungeon.world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
	}

	public static void render() {
		if (DRAW_DEBUG) {
			Graphics.batch.end();
			debug.render(world, Camera.instance.getCamera().combined);
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
	}

	/*
	 * Actual body creation
	 */


	public static Body createSimpleBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type) {
		return createSimpleBody(owner, x, y, w, h, type, false);
	}

	public static Body createSimpleBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type, boolean sensor) {
		BodyDef def = new BodyDef();
		def.type = type;

		Log.physics("Creating body for " + owner.getClass().getSimpleName() + " with params (" + x + ", " + y + ", " + w + ", " + h + ") and sensor = " + sensor);

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

		body.createFixture(fixture);
		body.setUserData(owner);
		poly.dispose();

		return body;
	}

	public static Body createSimpleCentredBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type) {
		return createSimpleCentredBody(owner, x, y, w, h, type, false);
	}

	public static Body createSimpleCentredBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type, boolean sensor) {
		BodyDef def = new BodyDef();
		def.type = type;

		Log.physics("Creating centred body for " + owner.getClass().getSimpleName() + " with params (" + x + ", " + y + ", " + w + ", " + h + ") and sensor = " + sensor);

		Body body = world.createBody(def);
		PolygonShape poly = new PolygonShape();

		poly.set(new Vector2[]{
			new Vector2(x - w / 2, y - h / 2), new Vector2(x + w / 2, y - h / 2),
			new Vector2(x - w / 2, y + h / 2), new Vector2(x + w / 2, y + h / 2)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = sensor;

		body.createFixture(fixture);
		body.setUserData(owner);
		poly.dispose();

		return body;
	}
}