package org.rexellentgames.dungeon.entity.creature.player;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Player extends Creature {
	{
		hpMax = 20;
	}

	private PointLight light;
	private Point vel;
	private int speed = 10;

	@Override
	public void init() {
		this.createBody(14, 14);

		this.light = new PointLight(this.area.getState().getLight(), 128, new Color(1, 1, 1f, 0.8f),
			512, 300, 300);
		this.light.setSoft(true);
		this.light.setSoftnessLength(32.0f);

		this.light.attachToBody(this.body);

		this.vel = new Point();

		Camera.instance.follow(this);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.vel.x *= 0.9;
		this.vel.y *= 0.9;

		if (Input.instance.isDown("left")) {
			this.vel.x -= this.speed;
		}

		if (Input.instance.isDown("right")) {
			this.vel.x += this.speed;
		}

		if (Input.instance.isDown("up")) {
			this.vel.y += this.speed;
		}

		if (Input.instance.isDown("down")) {
			this.vel.y -= this.speed;
		}

		this.body.setLinearVelocity(this.vel.x, this.vel.y);
	}

	@Override
	public void render() {
		Graphics.batch.draw(Graphics.sprites, this.x, this.y, 0, 0, 16, 16);
	}

	@Override
	public void destroy() {
		this.light.dispose();
	}
}