package org.rexcellentgames.burningknight.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Entity extends Point {
	protected Area area;
	public int depth = 0;
	public float w = 16;
	public float h = 16;
	public boolean onScreen = true;
	public boolean alwaysActive = false;
	public boolean alwaysRender = false;
	public boolean done = false;
	public int id;
	protected boolean active = true;

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}


	public void setId(int id) {
		this.id = id;
	}

	public float getDistanceTo(float x, float y) {
		float dx = x - this.x - this.w / 2;
		float dy = y - this.y - this.h / 2;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public float getAngleTo(float x, float y) {
		float dx = x - this.x - this.w / 2;
		float dy = y - this.y - this.h / 2;
		return (float) Math.atan2(dy, dx);
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getId() {
		return this.id;
	}

	public void init() {

	}

	public void destroy() {

	}

	public void update(float dt) {

	}

	public void render() {

	}

	public void renderShadow() {

	}

	public void setArea(Area area) {
		this.area = area;
	}

	public int getDepth() {
		return this.depth;
	}

	public Area getArea() {
		return this.area;
	}

	public void onCollision(Entity entity) {

	}

	public void onCollisionEnd(Entity entity) {

	}

	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		return true;
	}

	public boolean isOnScreen() {
		OrthographicCamera camera = Camera.game;

		float zoom = camera.zoom;

		return this.x + this.w * 2f >= camera.position.x - Display.GAME_WIDTH / 2 * zoom &&
			this.y + this.h * 2f >= camera.position.y - Display.GAME_HEIGHT / 2 * zoom &&
			this.x <= camera.position.x + Display.GAME_WIDTH / 2 * zoom &&
			this.y <= camera.position.y + this.h + Display.GAME_HEIGHT / 2 * zoom;
	}

	public long playSfx(String sound) {
		return playSfx(sound, 0.9f);
	}

	public long playSfx(String sound, float pitch) {
		if (this instanceof Player) {
			return Audio.playSfx(sound);
		}

		if (!this.onScreen) {
			return -1;
		}

		if (Player.instance == null) {
			return -1;
		}

		float d = this.getDistanceTo(Player.instance.x + 8, Player.instance.y + 8);

		if (d >= DISTANCE) {
			return -1;
		}

		return Audio.playSfx(sound, (DISTANCE - d) / DISTANCE, Math.min(1.5f, pitch + Random.newFloat(0.3f)));
	}

	private static final float DISTANCE = 256f;
}