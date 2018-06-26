package org.rexcellentgames.burningknight.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Log;

import java.util.ArrayList;

public class World {
  public static final float TIME_STEP = 1 / 100.0f;
  public static boolean DRAW_DEBUG = false;
  public static com.badlogic.gdx.physics.box2d.World world;
  private static Box2DDebugRenderer debug = new Box2DDebugRenderer();
  private static float accumulator;
  private static ArrayList<Body> toDestroy = new ArrayList<>();

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
      world.step(TIME_STEP, 6, 2);
      accumulator -= TIME_STEP;
    }

    if (!world.isLocked() && toDestroy.size() > 0) {
      Log.physics("Removing " + toDestroy.size() + " bodies");

      for (Body body: toDestroy) {
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
  }

  /*
   * Actual body creation
   */

  public static Body createSimpleBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type) {
    return createSimpleBody(owner, x, y, w, h, type, false);
  }

  public static Body createSimpleBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type, boolean sensor) {
    Log.physics("Creating body for " + owner.getClass().getSimpleName() + " with params (" + x + ", " + y + ", " + w + ", " + h + ") and sensor = " + sensor);

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

    body.createFixture(fixture);
    body.setUserData(owner);
    poly.dispose();

    return body;
  }

  public static Body createSimpleCentredBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type) {
    return createSimpleCentredBody(owner, x, y, w, h, type, false);
  }

  public static Body createSimpleCentredBody(Entity owner, float x, float y, float w, float h, BodyDef.BodyType type, boolean sensor) {
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
    fixture.isSensor = sensor;

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