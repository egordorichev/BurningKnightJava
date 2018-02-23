package org.rexellentgames.dungeon.entity.creature.player;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Player extends Creature {
	{
		hpMax = 20;
	}

	private static Animation idle = new Animation(Graphics.sprites, 0.1f, 0, 1, 2, 3, 4, 5, 6, 7);
	private static Animation run = new Animation(Graphics.sprites, 0.1f, 8, 9, 10, 11, 12, 13, 14, 15);

	private PointLight light;
	private Point vel;
	private boolean flipped = false;
	private int speed = 10;

	@Override
	public void init() {
		this.createBody(3, 1, 10, 10);

		this.light = new PointLight(this.area.getState().getLight(), 128, new Color(1, 1, 1f, 0.7f),
			512, 300, 300);
		this.light.setSoft(true);
		this.light.setSoftnessLength(16.0f);

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

		this.light.setPosition(this.x + 8, this.y + 8);
		this.body.setLinearVelocity(this.vel.x, this.vel.y);

		if (this.vel.x < 0) {
			this.flipped = true;
		} else if (this.vel.x > 0) {
			this.flipped = false;
		}

		this.vel.cap(100);

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (v > 9.9) {
			this.become("run");
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
		}
	}

	@Override
	public void render() {
		Animation animation;

		if (this.state.equals("run")) {
			animation = run;
		} else {
			animation = idle;
		}

		animation.render(this.x, this.y, this.t, this.flipped);
	}

	@Override
	public void destroy() {
		this.light.dispose();
	}
}