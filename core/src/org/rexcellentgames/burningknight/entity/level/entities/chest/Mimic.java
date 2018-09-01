package org.rexcellentgames.burningknight.entity.level.entities.chest;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.TFFx;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Mimic extends Mob {
	public static float chance = 20;
	public static ArrayList<Mimic> all = new ArrayList<>();
	private AnimationData closed;
	private AnimationData open;
	private AnimationData hurt;
	private AnimationData animation;
	private boolean found;
	public boolean weapon;
	public boolean locked = true;

	{
		hpMax = 30;
		h = 13;
		w = 18;
	}

	@Override
	public void initStats() {
		super.initStats();
		setStat("knockback", 0);
	}

	private int type = -1;

	@Override
	public void init() {
		super.init();

		all.add(this);

		this.body = World.createSimpleBody(this, 1, 0, 14, 13, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	protected void die(boolean force) {
		super.die(force);
		this.playSfx("death_clown");
		this.done = true;

		Chest chest = null;

		if (this.type == 1) {
			chest = new IronChest();
		} else if (type == 2) {
			chest = new GoldenChest();
		} else {
			chest = new WoodenChest();
		}

		chest.x = this.x;
		chest.y = this.y;
		chest.weapon = weapon;
		chest.locked = false;

		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		LevelSave.add(chest);

		chest.open();

		Achievements.unlock(Achievements.UNLOCK_MIMIC_TOTEM);
		Achievements.unlock(Achievements.UNLOCK_MIMIC_SUMMONER);
	}

	public void toChest() {
		Chest chest;

		if (this.type == 1) {
			chest = new IronChest();
		} else if (type == 2) {
			chest = new GoldenChest();
		} else {
			chest = new WoodenChest();
		}

		chest.x = this.x;
		chest.y = this.y;
		chest.locked = this.locked;
		chest.weapon = this.weapon;

		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		LevelSave.add(chest);

		this.done = true;
	}

	@Override
	public void destroy() {
		super.destroy();

		all.remove(this);
		this.body = World.removeBody(this.body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		found = reader.readBoolean();
		this.type = reader.readByte();
		weapon = reader.readBoolean();
		locked = reader.readBoolean();

		if (found) {
			this.become("found");
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(found);
		writer.writeByte((byte) this.type);
		writer.writeBoolean(weapon);
		writer.writeBoolean(locked);
	}

	private void readAnim() {
		if (this.animation == null) {
			Animation animations = WoodenChest.animation;

			if (this.type == -1) {
				Chest chest = Chest.random();

				if (chest instanceof IronChest) {
					this.type = 1;
				} else if (chest instanceof GoldenChest) {
					this.type = 2;
				} else if (chest instanceof WoodenChest) {
					this.type = 0;
				}
			}

			if (this.type == 1) {
				animations = IronChest.animation;
			} else if (type == 2) {
				animations = GoldenChest.animation;
			}

			closed = animations.get("idle");
			open = animations.get("opening_mimic");
			open.setAutoPause(true);

			hurt = animations.get("hurt");

			this.animation = this.closed;
		}
	}

	@Override
	public void render() {
		readAnim();


		Graphics.batch.setColor(1, 1, 1, this.a);
		this.renderWithOutline((this.invt > 0 && this.animation == open) ? hurt : this.animation);

		if (this.locked && !this.found) {
			float x = this.x + (w - Chest.idleLock.getRegionWidth()) / 2;
			float y = this.y + (h - Chest.idleLock.getRegionHeight()) / 2 +
				(float) Math.sin(this.t) * 1.8f;

			Graphics.render(Chest.idleLock, x, y);
		}

		renderStats();
	}

	{
		ignoreRooms = true;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.saw = true;

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		if (this.animation != null) {
			if (this.animation.update(dt * speedMod)) {
				if (this.animation == open) {
					this.become(this.animation.isGoingBack() ? "wait" : "attack");
				}
			}
		}

		if (this.hurt != null) {
			hurt.update(dt);
		}

		if (this.dead) {
			super.common();
			return;
		}

		super.common();
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player) {
			if (!this.found) {
				Achievements.unlock(Achievements.FIND_MIMIC);
				this.found = true;
				this.become("found");
			}
		}
	}

	@Override
	protected void onHurt(int a, Creature from) {
		super.onHurt(a, from);

		if (!this.found) {
			Achievements.unlock(Achievements.FIND_MIMIC);
			this.found = true;
			this.become("found");
		}
	}

	public class MimicState extends Mob.State<Mimic> {

	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "chase": case "idle": case "roam": return new IdleState();
			case "alerted": case "found": return new FoundState();
			case "attack": return new AttackState();
			case "close": return new CloseState();
			case "wait": return new WaitState();
		}

		return super.getAi(state);
	}

	public class IdleState extends MimicState {

	}

	public class FoundState extends MimicState {
		@Override
		public void onEnter() {
			super.onEnter();
			readAnim();

			animation = open;
			animation.setBack(false);
			animation.setPaused(false);
			animation.setFrame(0);
		}
	}

	@Override
	protected boolean canHaveBuff(Buff buff) {
		return !(buff instanceof PoisonBuff) && super.canHaveBuff(buff);
	}

	public class AttackState extends MimicState {
		private boolean did;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 1f && !did && self.target != null) {
				did = true;

				for (int i = 0; i < 10; i++) {
					PoofFx fx = new PoofFx();

					fx.x = self.x + self.w / 2;
					fx.y = self.y + self.h / 2;

					Dungeon.area.add(fx);
				}

				TFFx fx = new TFFx();

				fx.x = self.x + self.w / 2;
				fx.y = self.y + self.h / 2;

				fx.to(self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2));

				Dungeon.area.add(fx);
			}

			if (this.t >= 5f) {
				self.become("close");
			}
		}
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> drops = super.getDrops();

		for (int i = 0; i < Random.newInt(3, 8); i++) {
			ItemHolder item = new ItemHolder(new Gold());
			
			item.getItem().generate();

			Dungeon.area.add(item);
			LevelSave.add(item);
		}

		return drops;
	}

	public class CloseState extends MimicState {
		@Override
		public void onEnter() {
			super.onEnter();

			animation = open;
			open.setBack(true);
			open.setPaused(false);
			open.setFrame(4);
		}
	}

	public class WaitState extends MimicState {
		@Override
		public void onEnter() {
			super.onEnter();
			animation = closed;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 3f) {
				self.become("found");
			}
		}
	}

	@Override
	public boolean rollBlock() {
		if (!this.found) {
			this.found = true;
			this.become("found");
		}

		return !this.state.equals("attack") || super.rollBlock();
	}
}