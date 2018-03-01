package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.weapon.IronSword;
import org.rexellentgames.dungeon.entity.item.weapon.Sword;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.path.Graph;

import java.io.IOException;
import java.util.ArrayList;

public class Knight extends Mob {
	{
		hpMax = 10;
	}

	private static Animation idle = new Animation(Graphics.sprites, 0.08f, 16, 224, 225, 226, 227,
		228, 229, 230, 231);

	private static Animation run = new Animation(Graphics.sprites, 0.08f, 16, 232, 233, 234, 235,
		236, 237, 238, 239);

	private static Animation hurt = new Animation(Graphics.sprites, 0.1f, 16, 240, 241);
	private static Animation killed = new Animation(Graphics.sprites, 1f, 16, 242);

	private Point point;
	private Sword sword;
	private float timer;
	private float runDelay;

	@Override
	public void init() {
		super.init();

		this.speed = 10;
		this.runDelay = Random.newFloat(3f, 6f);
		this.sword = new IronSword();
		this.sword.setOwner(this);
		this.body = this.createBody(1, 2, 12, 14, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	private void attack() {
		if (this.sword.getDelay() == 0 && this.timer % 0.1 <= 0.017f) {
			this.sword.use();
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.vel.mul(0.8f);

		if (this.dead) {
			super.common();
			return;
		}

		this.sword.update(dt);
		this.timer += dt;

		if (!this.noticed) {
			this.vel.mul(0f);
		} else if (this.timer % this.runDelay <= 1f) {
			this.vel.mul(0);
		} else if (this.target != null) {
			if (!this.target.isDead()) {
				float dx = this.target.x - this.x;
				float dy = this.target.y - this.y;
				float d = (float) Math.sqrt(dx * dx + dy * dy);

				if (d < 24) {
					this.attack();
				} else {
					if (this.t % 0.5 <= 0.017) {
						this.point = this.getCloser(this.target);
					}

					if (this.point != null) {
						dx = this.point.x - this.x;
						dy = this.point.y - this.y;
						d = (float) Math.sqrt(dx * dx + dy * dy);

						if (d < 1) {
							this.x = this.point.x;
							this.y = this.point.y;

							this.point = this.getCloser(this.target);
						} else {
							this.vel.x += dx / d * this.speed;
							this.vel.y += dy / d * this.speed;
						}
					} else {
						this.point = this.getCloser(this.target);
					}
				}
			}
		} else {
			this.assignTarget();
		}

		// todo: cap
		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (v > 9.9) {
			this.become("run");
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
		}

		super.common();
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		Animation animation;

		if (this.dead) {
			animation = killed;
		} else if (this.invt > 0) {
			animation = hurt;
		} else if (this.state.equals("run")) {
			animation = run;
		} else {
			animation = idle;
		}

		animation.render(this.x, this.y, this.t, this.flipped);
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.sword.render(this.x, this.y, this.flipped);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(5)) {
			items.add(this.sword);
		}

		return items;
	}
}