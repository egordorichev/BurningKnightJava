package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.Dagger;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

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
		hpMax = 3;
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

	protected Item stolen;

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h, 0, this.a);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		if (reader.readBoolean()) {
			try {
				String name = reader.readString();
				this.stolen = (Item) Class.forName(name).newInstance();
				this.become("took");
			} catch (Exception e) {

			}
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(stolen != null);

		if (stolen != null) {
			writer.writeString(stolen.getClass().getName());
		}
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
		this.sword.render(this.x, this.y, this.w, this.h, this.flipped);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	public class ThiefState extends Mob.State<Thief> {

	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": return new IdleState();
			case "chase": case "alerted": return new ChaseState();
			case "unchase": return new UnchaseState();
			case "attack": return new AttackState();
			case "preattack": return new PreattackState();
			case "wait": return new WaitState();
			case "took": return new TookState();
		}

		return super.getAi(state);
	}

	public class TookState extends ThiefState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.setStat("block_chance", 0.1f);
		}

		@Override
		public void onExit() {
			super.onExit();
			self.setStat("block_chance", 0.2f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			this.checkForPlayer();
			self.checkForRun();

			if (self.lastSeen != null) {
				this.moveFrom(self.lastSeen, 8f, 10f);
			}
		}
	}

	public class IdleState extends ThiefState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.stolen != null) {
				self.become("took");
				return;
			}

			this.checkForPlayer();
			self.checkForRun();
		}
	}

	public class ChaseState extends ThiefState {
		public static final float ATTACK_DISTANCE = 24f;

		@Override
		public void update(float dt) {
			if (self.stolen != null) {
				self.become("took");
				return;
			}

			this.checkForPlayer();

			if (self.lastSeen == null) {
				return;
			} else {
				if (this.moveTo(self.lastSeen, 8f, ATTACK_DISTANCE)) {
					if (self.target != null) {
						self.become("preattack");
					} else {
						self.noticeSignT = 0f;
						self.hideSignT = 2f;
						self.become("idle");
					}

					return;
				}
			}

			super.update(dt);
		}
	}

	public void checkForRun() {
		if (this.target == null) {
			return;
		}

		float d = this.getDistanceTo(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);

		if (d < 128f) {
			this.become("unchase");
		}
	}

	public class UnchaseState extends ThiefState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(2f, 5f);
			self.setStat("block_chance", 0.1f);
		}

		@Override
		public void onExit() {
			super.onExit();
			self.setStat("block_chance", 0.2f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();

			if (this.t >= delay) {
				self.become("wait");
			} else if (self.lastSeen != null) {
				this.moveFrom(self.lastSeen, 7f, 10f);
			}
		}
	}

	public class WaitState extends ThiefState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(1f, 3f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			this.checkForPlayer();
			self.checkForRun();
			if (this.t >= delay) {
				self.become("chase");
			}
		}
	}

	public class AttackState extends ThiefState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.sword.use();
			self.become(Random.chance(40) ? "unchase" : "chase");
		}
	}


	@Override
	protected void onHurt(int a, Creature creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_thief");
	}

	@Override
	public void onHit(Creature who) {
		super.onHit(who);

		if (stolen == null && who instanceof Player && Random.chance(70)) {
			Player player = (Player) who;

			for (int i = 0; i < 6; i++) {
				if (i != player.getInventory().active) {
					Item item = player.getInventory().getSlot(i);

					if (item != null && !item.isCursed() && Random.chance(30)) {
						Log.info("Stolen " + item.getName());
						stolen = item;
						stolen.setOwner(this);
						player.getInventory().setSlot(i, null);
						this.become("took");
						break;
					}
				}
			}
		}
	}

	public class PreattackState extends ThiefState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 1.3f) {
				self.become("attack");
			}
		}
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
	protected void die(boolean force) {
		super.die(force);

		this.playSfx("death_thief");

		this.done = true;
		deathEffect(killed);

		if (stolen != null) {
			ItemHolder holder = new ItemHolder(this.stolen);
			
			holder.x = this.x + (this.w - holder.w) / 2;
			holder.y = this.y + (this.h - holder.h) / 2;
			
			Dungeon.area.add(holder);
		}
	}
}