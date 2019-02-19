package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.Dagger;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Thief extends Mob {
	public static Animation animations = Animation.make("actor-thief", "-purple");
	protected Item sword;
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 5;
		w = 15;

		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
	}

	@Override
	public void initStats() {
		super.initStats();
		this.setStat("block_chance", 0.2f);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h, 0, this.a);
	}

	@Override
	public void init() {
		super.init();

		this.sword = new Dagger();
		this.sword.setOwner(this);

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

		speed = 150;
		maxSpeed = 150;
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		} else if (target != null) {
			flipped = target.x < x;
		}

		float v = Math.abs(this.acceleration.x) + Math.abs(this.acceleration.y);

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 1f) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);

		this.sword.render(this.x, this.y, this.w, this.h, this.flipped, false);

		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();

		// Graphics.print(this.state, Graphics.small, this.x, this.y);
	}

	public class ThiefState extends Mob.State<Thief> {

	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": case "alerted": return new IdleState();
			case "attack": return new AttackState();
		}

		return super.getAi(state);
	}

	public Vector2 direction = new Vector2();

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (state.equals("attack") && (entity == null || entity instanceof Level || entity instanceof RollingSpike || entity instanceof SolidProp || entity instanceof Door)) {
			become("idle");
		}
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity == null || entity instanceof Level || entity instanceof RollingSpike || entity instanceof SolidProp || entity instanceof Door) {
			return true;
		}

		return super.shouldCollide(entity, contact, fixture);
	}

	public class AttackState extends ThiefState {
		@Override
		public void update(float dt) {
			super.update(dt);
			float f = Math.min(40f, t * 40f);
			acceleration.x = direction.x * f;
			acceleration.y = direction.y * f;
		}
	}

	public class IdleState extends ThiefState {
		@Override
		public void update(float dt) {
			this.checkForPlayer();

			if (self.target != null && self.target.room == self.room) {
				float dx = self.target.x + self.target.w / 2 - self.x - self.w / 2;
				float dy = self.target.y + self.target.h / 2 - self.y - self.h / 2;

				float d = 16;

				if (Math.abs(dy) < d || Math.abs(dx) < d) {
					if (Math.abs(dx) > Math.abs(dy)) {
						direction.x = dx < 0 ? -1 : 1;
						direction.y = 0;
					} else {
						direction.y = dy < 0 ? -1 : 1;
						direction.x = 0;
					}

					self.become("attack");
				}
			}
		}
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(5)) {
			items.add(new Dagger());
		}

		return items;
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_thief");
	}

	@Override
	public void destroy() {
		super.destroy();
		this.sword.destroy();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		this.sword.update(dt * speedMod);
		super.common();
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_thief");
		deathEffect(killed);
	}

	@Override
	public void onHit(Creature who) {
		super.onHit(who);

		if (who instanceof Player) {
			Player player = (Player) who;

			if (player.getMoney() > 0) {
				int amount = Random.newInt(Math.min(player.getMoney(), 5), Math.min(player.getMoney(), 10));
				player.setMoney(player.getMoney() - amount);

				for (int i = 0; i < amount; i++) {
					ItemHolder holder = new ItemHolder();

					holder.x = this.x + w / 2 + Random.newFloat(-4, 4);
					holder.y = this.y + h / 2 + Random.newFloat(-4, 4);
					holder.setItem(new Gold());

					Dungeon.area.add(holder.add());
					holder.randomVelocity();
				}
			}
		}
	}
}