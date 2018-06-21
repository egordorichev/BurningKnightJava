package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.plant.Plant;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class FireballProjectile extends Projectile {
	private static Animation animations = Animation.make("fx-fireball");
	private AnimationData born = animations.get("appear");
	private AnimationData idle = animations.get("idle");
	private AnimationData dead = animations.get("dead");
	private AnimationData animation = born;

	public Creature target;

	{
		alwaysActive = true;
		depth = 11;
	}

	@Override
	protected boolean hit(Entity entity) {
		if (entity instanceof Plant) {
			((Plant) entity).startBurning();
			return false;
		}

		if (this.bad) {
			if (entity instanceof Player) {
				this.doHit(entity);
				return true;
			}
		} else if (entity instanceof Mob) {
			this.doHit(entity);
			return true;
		}

		return false;
	}

	@Override
	protected void death() {
		this.animation = dead;
		this.dead.setListener(new AnimationData.Listener() {
			@Override
			public void onEnd() {
				setDone(true);
			}
		});
	}

	@Override
	public void update(float dt) {
		this.animation.update(dt);
		super.update(dt);
	}

	@Override
	public void init() {
		super.init();
		this.playSfx("fireball_cast");

		this.t = Random.newFloat(32f);

		this.body = World.createCircleCentredBody(this, 0, 0, 6, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
		this.body.setBullet(true);

		if (this.target != null) {
			float dx = this.target.x + this.target.w / 2 - this.x - 5;
			float dy = this.target.y + this.target.h / 2 - this.y - 5;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			this.vel.x = dx / d * 3;
			this.vel.y = dy / d * 3;

			this.body.setLinearVelocity(this.vel);
		}

		this.born.setListener(new AnimationData.Listener() {
			@Override
			public void onEnd() {
				animation = idle;
			}
		});
	}

	@Override
	public void render() {
		TextureRegion texture = this.animation.getCurrent().frame;

		float sx = (float) (1f + Math.cos(this.t * 7) / 6f);
		float sy = (float) (1f + Math.sin(this.t * 6f) / 6f);

		Graphics.render(texture, this.x, this.y,
			0, texture.getRegionWidth() / 2, texture.getRegionHeight() / 2, false, false, sx, sy);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x - 8, this.y, this.w, this.h, 5);
	}
}