package org.rexcellentgames.burningknight.entity.creature.mob.tech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Vacuum extends Bot {
	public static Animation animations = Animation.make("actor-vacum", "-normal");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		animation = idle;
	}

	@Override
	public void knockBackFrom(Entity from, float force) {

	}

	@Override
	public void init() {
		super.init();

		w = 14;
		h = 11;

		body = World.createSimpleBody(this, 2, 2, w - 4, h - 4, BodyDef.BodyType.DynamicBody, false);
		body.getFixtureList().get(0).setRestitution(1f);
		body.setTransform(x, y, 0);

		float f = 20;

		this.velocity = new Point(f * (Random.chance(50) ? -1 : 1), f * (Random.chance(50) ? -1 : 1));

		body.setLinearVelocity(this.velocity);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else {
			this.animation = idle;
		}

		Graphics.startAlphaShape();
		float dt = Dungeon.game.getState().isPaused() ? 0 : Gdx.graphics.getDeltaTime();

		for (int i = parts.size() - 1; i >= 0; i--) {
			SuckParticle p = parts.get(i);

			p.t += dt;

			Graphics.shape.setColor(1, 1, 1, Math.min(1, p.t * 2f));
			float d = 24 - p.t * p.s * 8;

			if (d <= 2f) {
				parts.remove(i);
				continue;
			}

			Graphics.shape.rect(this.x + w / 2 + (float) Math.cos(p.a) * d, this.y + h / 2 + (float) Math.sin(p.a) * d, p.s / 2, p.s / 2, p.s,
				p.s, 1, 1, p.an);
		}

		tm += dt;

		if (tm >= 0.1f) {
			tm = 0;
			parts.add(new SuckParticle());
		}

		Graphics.endAlphaShape();

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	private float tm;
	private ArrayList<SuckParticle> parts = new ArrayList<>();

	@Override
	public void renderShadow() {
		Graphics.shadow(x + 1, y + 2, w - 1, h, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.body != null) {
			this.velocity.x = this.body.getLinearVelocity().x;
			this.velocity.y = this.body.getLinearVelocity().y;

			float a = (float) Math.atan2(this.velocity.y, this.velocity.x);
			this.body.setLinearVelocity(((float) Math.cos(a)) * 20 * Mob.speedMod, ((float) Math.sin(a)) * 20 * Mob.speedMod);
		}

		if (Player.instance.room == room) {
			this.suck(Player.instance, dt);
		}


		animation.update(dt);
		super.common();
	}

	private void suck(Creature creature, float dt) {
		float dx = creature.x + creature.w / 2 - this.x - this.w / 2;
		float dy = creature.y + creature.h / 2 - this.y - this.h / 2;
		float d = (float) Math.sqrt(dx * dx + dy * dy);
		float max = 64;

		if (d < max) {
			float mod = (64 - d) * 20 * dt;
			creature.velocity.x -= dx / d * (mod);
			creature.velocity.y -= dy / d * (mod);
		}
	}

	private class SuckParticle {
		public float t;
		public float a;
		public float s;
		public float an;
		public float sp;

		public SuckParticle() {
			a = Random.newAngle();
			s = Random.newFloat(1, 5);
			an = Random.newFloat(360);
			sp = Random.newFloat(0.5f, 1.5f);
		}
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();

		this.playSfx("death_clown");
		deathEffect(killed);
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	protected Mob.State getAi(String state) {
		return new IdleState();
	}

	public class IdleState extends Mob.State<Vacuum> {

	}
}