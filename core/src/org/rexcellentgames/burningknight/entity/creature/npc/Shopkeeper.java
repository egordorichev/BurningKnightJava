package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.ShopSale;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.BronzeShotgun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.Shotgun;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class Shopkeeper extends Npc {
	private static Animation animations = Animation.make("actor-trader", "-green");
	private AnimationData idle = animations.get("idle");
	private AnimationData run = animations.get("run");
	private AnimationData hurt = animations.get("hurt");
	private AnimationData death = animations.get("death");
	private AnimationData animation = idle;
	public static Shopkeeper instance;

	{
		w = 15;
		h = 15;
	}

	/*
	 * AI plan:
	 *o - When not enranged:
	 *o + Welcomes you with a random message
	 *o + Can stand somewhere for a while
	 *o + Can start following you for a short while
	 *o + Might go to a stand with an item and say something about it
	 *o * Getting hurt from player enrages him, or going away from the shop without paying for the item
	 * - When enranged:
	 * + Starts running around really fast
	 * + Sometimes stops, aims at you and shoots
	 * + On death drops a lot of coins
	 * + All items become free
	 * + All other shopkeepers become enraged
	 */

	public boolean enranged;

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> drops = super.getDrops();

		if (Random.chance(20)) {
			drops.add(new ShopSale());
		}

		for (int i = 0; i < Random.newInt(3, 8); i++) {
			drops.add(new Gold());
		}

		return drops;
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		enranged = reader.readBoolean();

		if (enranged) {
			this.become("alerted");
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(enranged);
	}

	@Override
	public void init() {
		super.init();
		this.body = World.createSimpleBody(this, 4, 0, 8, 14, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
		instance = this;
	}

	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}

	@Override
	public void die() {
		super.die();

		for (ItemHolder holder : ItemHolder.all) {
			holder.getItem().shop = false;
		}
	}

	@Override
	public void update(float dt) {
		/*if (!(this.state.equals("idle") || this.state.equals("roam")) && Player.instance.room != this.room) {
			for (int i = 0; i < Player.instance.getInventory().getSize(); i++) {
				Item item = Player.instance.getInventory().getSlot(i);

				if (item != null && item.shop) {
					this.enranged = true;
					this.become("hana");
					break;
				}
			}
		}*/

		super.update(dt);

		if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.vel.len2() >= 20f) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.vel.x) > 1f) {
			this.flipped = this.vel.x < 0;
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		if (this.shotgun != null) {
			this.shotgun.update(dt * speedMod);
		}
	}

	@Override
	protected void die(boolean force) {
		super.die(force);
		this.playSfx("death_towelknight");
		this.done = true;

		deathEffect(death);
	}

	@Override
	public void render() {
		animation.render(this.x, this.y, this.flipped);

		if (this.shotgun != null) {
			this.shotgun.render(this.x, this.y, this.w, this.h, this.flipped);
		}

		// Graphics.print(this.state, Graphics.small, this.x, this.y);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x + 2, this.y, this.w - 4, this.h);
	}

	public class SKState extends Mob.State<Shopkeeper> {

	}

	public class IdleState extends SKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (Player.instance.room == self.room) {
				self.become("hi");
			}
		}
	}

	public class HelpState extends SKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.room != Player.instance.room) {
				self.become("idle");
			} else if (this.moveTo(new Point(Player.instance.x + Player.instance.w / 2, Player.instance.y + Player.instance.h / 2), 5f, 24f)) {
				self.become("stand");
			}
		}
	}

	private static String[] messages = {
		"hi", "bad_to_see_you", "still_alive", "buy_something", "welcome"
	};

	public class HiState extends SKState {
		private NpcDialog dialog;
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(5f, 10f);

			dialog = new NpcDialog(self, Locale.get(messages[Random.newInt(messages.length)]));
			dialog.open();
			Dungeon.area.add(dialog);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.room != Player.instance.room) {
				self.become("idle");
			} else if (this.t >= delay) {
				self.become("stand");
			}
		}

		@Override
		public void onExit() {
			super.onExit();
			dialog.remove();
		}
	}

	private static String[] itemMessages = {
		"look_at_this", "so_pretty", "only_for_you", "must_have", "buy_this"
	};

	public class TalkState extends SKState {
		private NpcDialog dialog;
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(7f, 12f);

			dialog = new NpcDialog(self, Locale.get(itemMessages[Random.newInt(itemMessages.length)]));
			dialog.open();
			Dungeon.area.add(dialog);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.room != Player.instance.room) {
				self.become("idle");
			} else if (this.t >= delay) {
				self.become("stand");
			}
		}

		@Override
		public void onExit() {
			super.onExit();
			dialog.remove();
		}
	}


	private static String[] thanksMessages = {
		"thanks", "nice_choice", "great", "$$$"
	};

	public class ThanksState extends SKState {
		private NpcDialog dialog;
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(7f, 12f);

			dialog = new NpcDialog(self, Locale.get(thanksMessages[Random.newInt(thanksMessages.length)]));
			dialog.open();
			Dungeon.area.add(dialog);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.room != Player.instance.room) {
				self.become("idle");
			} else if (this.t >= delay) {
				self.become("help");
			}
		}

		@Override
		public void onExit() {
			super.onExit();
			dialog.remove();
		}
	}

	public class StandState extends SKState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(3, 8f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);


			if (self.room != Player.instance.room) {
				self.become("idle");
			} else if (this.t >= delay) {
				self.become(new String[] { "help", "walk", "sell" }[Random.newInt(3)]);
			}
		}
	}

	public class WalkState extends SKState {
		private float delay;
		private Point to;

		@Override
		public void onEnter() {
			super.onEnter();
			this.delay = Random.newFloat(5, 10);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (to == null) {
				to = self.room.getRandomFreeCell();
				to.x *= 16;
				to.y *= 16;
			}

			if (this.moveTo(this.to, 2f, 16f)) {
				self.become("stand");
			}

			if (this.t >= this.delay) {
				self.become("stand");
			}
		}
	}

	public class SellState extends SKState {
		private Point to;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (to == null) {
				ArrayList<ItemHolder> list = new ArrayList<>();

				for (ItemHolder holder : ItemHolder.all) {
					if (holder.getItem().shop) {
						list.add(holder);
					}
				}

				if (list.size() > 0) {
					ItemHolder target = list.get(Random.newInt(list.size()));

					to = new Point();

					to.x = target.x + (self.w - target.w) / 2;
					to.y = target.y + 24;
					Log.info("found");
				} else {
					Log.error("not found");
					self.become("stand");
					return;
				}
			}

			if (this.moveTo(this.to, 3f, 16f)) {
				self.become("talk");
			}
		}
	}

	public class HanaState extends SKState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.enranged = true;
			self.shotgun = new BronzeShotgun();
			self.shotgun.setOwner(self);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 1f) {
				this.t = 0f;
				self.shotgun.use();
			}
		}
	}

	public Shotgun shotgun;

	@Override
	protected State getAi(String state) {
		if (enranged) {
			return new HanaState();
		}

		switch (state) {
			case "alerted": case "chase": case "idle": case "roam": return new IdleState();
			case "help": return new HelpState();
			case "hana": return new HanaState();
			case "hi": return new HiState();
			case "stand": return new StandState();
			case "walk": return new WalkState();
			case "sell": return new SellState();
			case "talk": return new TalkState();
			case "thanks": return new ThanksState();
		}

		return super.getAi(state);
	}

	@Override
	protected void onHurt(float a, Creature from) {
		super.onHurt(a, from);

		if (from instanceof Player) {
			this.become("hana");
		}
	}
}