package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;

public class WormholeFx extends Entity {
	private Point vel = new Point();
	public static ArrayList<Suckable> suck = new ArrayList<>();
	private static TextureRegion sprite = Graphics.getTexture("actor-bomb-idle-00"); // todo: replace

	@Override
	public void init() {
		super.init();

		float dx = Input.instance.worldMouse.x - this.x;
		float dy = Input.instance.worldMouse.y - this.y;

		float a = (float) Math.atan2(dy, dx);

		this.vel.x = (float) (Math.cos(a) * 200f);
		this.vel.y = (float) (Math.sin(a) * 200f);
	}

	@Override
	public void render() {
		Graphics.render(sprite, this.x, this.y);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		for (Suckable entity : suck) {
			Body body = entity.getBody();

			float dx = this.x + 8 - body.getPosition().x - 5;
			float dy = this.y + 8 - body.getPosition().y - 5;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d >= 1f && d <= 400f) {
				Vector2 vel = body.getLinearVelocity();

				vel.x += dx / d * 60;
				vel.y += dy / d * 60;

				body.setLinearVelocity(vel);
			}
		}
	}

	interface Suckable {
		Body getBody();
	}
}