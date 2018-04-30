package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Entity extends Point {
	protected Area area;
	protected int depth = 0;
	public float w = 16;
	public float h = 16;
	public boolean onScreen = true;
	public boolean alwaysActive = false;
	public boolean alwaysRender = false;
	public boolean done = false;
	public int id;

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

	public void renderTop() {

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

	public boolean isOnScreen() {
		OrthographicCamera camera = Camera.instance.getCamera();

		float zoom = camera.zoom;

		return this.x + this.w >= camera.position.x - Display.GAME_WIDTH / 2 * zoom &&
			this.y + this.h >= camera.position.y - Display.GAME_HEIGHT / 2 * zoom &&
			this.x <= camera.position.x + Display.GAME_WIDTH / 2 * zoom &&
			this.y <= camera.position.y + this.h + Display.GAME_HEIGHT / 2 * zoom;
	}

	public long playSfx(Sound sound) {
		if (this instanceof Player) {
			return sound.play();
		}

		if (!this.onScreen) {
			return -1;
		}

		float d = this.getDistanceTo(Player.instance.x + 8, Player.instance.y + 8);

		if (d >= 128f) {
			return -1;
		}

		return sound.play((128f - d) / 128f);
	}
}