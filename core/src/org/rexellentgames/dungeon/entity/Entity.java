package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Entity extends Point {
	protected Area area;
	protected int depth = 0;
	public float w = 16;
	public float h = 16;
	public boolean onScreen = true;
	public boolean alwaysActive = false;
	public boolean done = false;

	public void init() {

	}

	public void destroy() {

	}

	public void update(float dt) {

	}

	public void render() {

	}

	public void setArea(Area area) {
		this.area = area;
	}

	public int getDepth() {
		return this.depth;
	}

	protected Body createBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		World world = this.area.getState().getWorld();

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
		body.setUserData(this);
		poly.dispose();

		return body;
	}

	public void onCollision(Entity entity) {

	}

	public void onCollisionEnd(Entity entity) {

	}

	public boolean isOnScreen() {
		OrthographicCamera camera = Camera.instance.getCamera();

		return this.x + this.w >= camera.position.x - Display.GAME_WIDTH / 2 &&
			this.y + this.h >= camera.position.y - Display.GAME_HEIGHT / 2 &&
			this.x <= camera.position.x + Display.GAME_WIDTH / 2 &&
			this.y <= camera.position.y + Display.GAME_HEIGHT / 2;
	}
}