package org.rexellentgames.dungeon.entity.creature.mob;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.weapon.Sword;
import org.rexellentgames.dungeon.entity.item.weapon.TheSword;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

public class BurningKnight extends Mob {
	{
		hpMax = 10000;
		damage = 10;
		w = 32;
		h = 32;
		depth = 6;
		alwaysActive = true;
		flying = true;
	}

	public static BurningKnight instance;

	private static Animation idle = Animation.make(Graphics.sprites, 0.08f, 32, 160, 162,
		164, 166, 168, 170, 172, 174, 176, 178, 180, 182);
	private static Animation hurt = Animation.make(Graphics.sprites, 0.1f, 32, 184, 186);
	private static Animation killed = Animation.make(Graphics.sprites, 0.1f, 32, 188);
	private Sword sword;

	public void tpToPlayer() {
		if (Player.instance != null) {
			Camera.instance.follow(Player.instance);
		}

		double a = Random.newFloat((float) (Math.PI * 2));
		this.tp((float) (Player.instance.x + Math.cos(a) * 128), (float) (Player.instance.y + Math.sin(a) * 128));
	}

	@Override
	public void init() {
		instance = this;
		super.init();

		this.sword = new TheSword();
		this.sword.setOwner(this);

		this.body = this.createBody(8, 3, 16, 18, BodyDef.BodyType.DynamicBody, true);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 16, this.y + 16, 0, 0, 0.3f, 0.5f, 8f, true);
		}

		this.sword.update(dt);

		if (this.target == null) {
			this.assignTarget();
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.invt > 0) {
			this.common();
			return;
		}

		if (this.target != null && !this.target.invisible) {
			float dx = this.target.x - this.x - 8;
			float dy = this.target.y - this.y - 8;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d > 16) {
				d /= (this.timer % 10f <= 1f ? 10 : 2);

				this.vel.x += dx / d;
				this.vel.y += dy / d;
			} else if (this.sword.getDelay() == 0 && this.timer % 1f <= 0.0175f) {
				this.sword.use();
			}
		}

		super.common();
	}

	@Override
	protected void onHurt() {
		this.vel.mul(0f);
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		Animation animation;

		if (this.dead) {
			animation = killed;
		} else if (this.invt > 0) {
			animation = hurt;
		} else {
			animation = idle;
		}

		animation.render(this.x, this.y, this.t, this.flipped);
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.sword.render(this.x, this.y, this.flipped);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}