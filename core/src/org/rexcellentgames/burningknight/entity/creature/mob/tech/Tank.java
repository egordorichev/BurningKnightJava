package org.rexcellentgames.burningknight.entity.creature.mob.tech;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Mine;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Tank extends Bot {
	public static Animation animations = Animation.make("actor-tank", "-normal");
	private AnimationData moveHor;
	private AnimationData moveVert;
	private AnimationData killed;
	private AnimationData hurtHor;
	private AnimationData hurtVert;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		moveVert = getAnimation().get("vert");
		hurtVert = getAnimation().get("hurt_vert");
		moveHor = getAnimation().get("horiz");
		hurtHor = getAnimation().get("hurt_horiz");
		killed = getAnimation().get("dead");
		animation = moveHor;

		w = 20;
		h = 24;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Level || entity == null || entity instanceof Door || entity instanceof SolidProp || entity instanceof RollingSpike) {
			((IdleState)this.ai).selectDir();
		}

		super.onCollision(entity);
	}

	@Override
	public float getOy() {
		return 11;
	}

	private boolean vert;

	@Override
	public void render() {
		vert = this.acceleration.y != 0;

		if (vert) {
			flipped = false;
			this.flippedVert = this.acceleration.y < 0;
		} else {
			flippedVert = false;
			this.flipped = this.acceleration.x > 0;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = vert ? hurtVert : hurtHor;
		} else {
			this.animation = vert ? moveVert : moveHor;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		animation.update(dt);
		super.common();
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
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": case "alerted": return new IdleState();
		}

		return super.getAi(state);
	}

	@Override
	public void renderShadow() {
		if (vert) {
			Graphics.shadow(x + 3, y + (flippedVert ? 7 : 6), 14, h, 0);
		} else {
			Graphics.shadow(x + (flipped ? 4 : 0), y + 3, 16, h, 0);
		}
	}

	public class IdleState extends Mob.State<Tank> {
		@Override
		public void onEnter() {
			super.onEnter();
			selectDir();
		}

		private float delay;
		private Point dir;
		private float last;

		public void selectDir() {
			t = 0;
			delay = Random.newFloat(5, 10);

			if (dir == null) {
				boolean vert = Random.chance(50);
				dir = new Point(vert ? 0 : Random.chance(50) ? -1 : 1, !vert ? 0 : Random.chance(50) ? -1 : 1);
				return;
			}

			self.velocity.x -= dir.x * 60;
			self.velocity.y -= dir.y * 60;

			if (dir.x != 0) {
				dir.x = 0;
				dir.y = Random.chance(50) ? -1 : 1;
			} else {
				dir.y = 0;
				dir.x = Random.chance(50) ? -1 : 1;
			}

			if (last <= 0 && Player.instance.room == self.room) {
				Mine mine = new Mine();
				mine.x = self.x;
				mine.y = self.y;
				Dungeon.area.add(mine.add());

				last = 1f;
			}
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			last -= dt;

			if (t >= delay) {
				selectDir();
			}

			float s = 10;

			self.acceleration.x = dir.x * s;
			self.acceleration.y = dir.y * s;
		}
	}
}