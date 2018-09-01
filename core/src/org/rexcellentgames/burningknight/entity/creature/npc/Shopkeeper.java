package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
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
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.ShopSale;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.BronzeShotgun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.Shotgun;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class Shopkeeper extends Npc {
	private AnimationData idle = getAnimation().get("idle");
	private AnimationData run = getAnimation().get("run");
	private AnimationData hurt = getAnimation().get("hurt");
	private AnimationData death = getAnimation().get("death");
	private AnimationData animation = idle;
	public static Shopkeeper instance;

	private static Animation animations = Animation.make("actor-trader", "-green");

	public Animation getAnimation() {
		return animations;
	}

	{
		ignoreRooms = true;
		w = 15;
		h = 15;
		speed = 7;
		maxSpeed = 200;
		hpMax = 20;
	}

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
	public void initStats() {
		super.initStats();
		modifyStat("gun_use_time", 1);
		modifyStat("reload_time", 1);
	}

	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}

	public static Dialog dialogs = Dialog.make("shop-keeper");
	public static DialogData wannaBuy = dialogs.get("wanna-buy");
	public static DialogData buy = dialogs.get("buy");
	public static DialogData stupid = dialogs.get("stupid");

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

		lastWhite = (!this.talking && Player.instance.pickupFx == null && Player.instance.room == this.room && Player.instance.getDistanceTo(this.x + this.w / 2, this.y + this.h / 2) < 10);

		if (lastWhite && Input.instance.wasPressed("interact")) {
			talking = true;
			this.become("talk_dialog");
			int id = Player.instance.ui.getActive();

			Item item = Player.instance == null ? null : Player.instance.getInventory().getSlot(id);

			int price = item == null ? 0 : item.getPrice() * item.getCount();

			Dialog.active = item == null ? wannaBuy : (item instanceof Gold ? stupid : buy);
			Dialog.active.start();

			if (item != null) {
				Dialog.active.setVariable("item", item.getName());
				Dialog.active.setVariable("gold", price + "");
			}

			Dialog.active.onEnd(new Runnable() {
				@Override
				public void run() {
					talking = false;
					become("idle");
				}
			});

			if (item != null) {
				Dialog.active.onSelect(new Runnable() {
					@Override
					public void run() {
						if (Dialog.active.getSelected() == 0) {
							Player.instance.getInventory().setSlot(id, null);
							ItemHolder gold = new ItemHolder(new Gold());

							gold.getItem().setCount(price);

							Player.instance.tryToPickup(gold);

							int val = GlobalSave.getInt("num_sold_items") + 1;
							GlobalSave.put("num_sold_items", val);

							Achievements.unlock(Achievements.UNLOCK_GOLD_RING);

							if (val >= 10) {
								Achievements.unlock(Achievements.SELL_10_ITEMS);
							}
						}
					}
				});
			}
		}

		if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.velocity.len2() >= 20f) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
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
			this.shotgun.updateInHands(dt * speedMod);
		}
	}

	@Override
	protected void die(boolean force) {
		super.die(force);
		this.playSfx("death_towelknight");
		this.done = true;

		deathEffect(death);
		Achievements.unlock(Achievements.UNLOCK_SALE);
	}

	private float al;
	private boolean talking;
	private boolean lastWhite;

	@Override
	public void render() {
		if (!this.enranged) {
			float dt = Gdx.graphics.getDeltaTime();
			this.al = MathUtils.clamp(0, 1, this.al + ((lastWhite ? 1 : 0) - this.al) * dt * 10);

			if (this.al > 0) {
				Graphics.batch.end();
				Mob.shader.begin();
				Mob.shader.setUniformf("u_color", new Vector3(1, 1, 1));
				Mob.shader.setUniformf("u_a", this.al);
				Mob.shader.end();
				Graphics.batch.setShader(Mob.shader);
				Graphics.batch.begin();

				for (int xx = -1; xx < 2; xx++) {
					for (int yy = -1; yy < 2; yy++) {
						if (Math.abs(xx) + Math.abs(yy) == 1) {
							animation.render(this.x + xx, this.y + yy, this.flipped);
						}
					}
				}

				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}
		}

		animation.render(this.x, this.y, this.flipped);

		if (this.shotgun != null) {
			this.shotgun.render(this.x, this.y, this.w, this.h, this.flipped);
		}

		super.renderStats();

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
				self.become(self.enranged ? "hana" : "hi");
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

				for (ItemHolder holder : ItemHolder.getAll()) {
					if (holder.getItem().shop) {
						list.add(holder);
					}
				}

				if (list.size() > 0) {
					ItemHolder target = list.get(Random.newInt(list.size()));

					to = new Point();

					to.x = target.x + (self.w - target.w) / 2;
					to.y = target.y + 24;
				} else {
					self.become("stand");
					return;
				}
			}

			if (this.moveTo(this.to, 3f, 16f)) {
				self.become("talk");
			}
		}
	}

	public void enrage() {
		if (enranged) {
			return;
		}

		enranged = true;
		this.become("hana");

		for (ItemHolder holder : ItemHolder.getAll()) {
			holder.getItem().shop = false;
			if (holder.getPrice() != null) {
				holder.getPrice().remove();
				holder.setPrice(null);
			}
		}

		shotgun = new BronzeShotgun();
		shotgun.setOwner(this);
	}

	public class HanaState extends SKState {
		@Override
		public void onEnter() {
			super.onEnter();
			enrage();
		}

		private Point to;

		@Override
		public void update(float dt) {
			super.update(dt);

			this.tt += dt;

			if (self.shotgun == null) {
				shotgun = new BronzeShotgun();
				shotgun.setOwner(self);
			}

			if (this.t >= 3f) {
				if (this.t >= 3.3f) {
					this.t = 0f;

					self.shotgun.use();
					self.shotgun.setAmmoLeft(20);
				}
			} else if (Player.instance != null && Player.instance.room != null) {
				if (to == null) {
					to = Player.instance.room.getRandomFreeCell();
					to.x *= 16;
					to.y *= 16;
				}

				if (this.moveTo(to, 12f, 32f) || this.tt >= 5f) {
					to = null;
					this.tt = 0;
				}
			}
		}

		private float tt;
	}

	public Shotgun shotgun;

	@Override
	protected State getAi(String state) {
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
			case "talk_dialog": return new TalkDialogState();
		}

		return super.getAi(state);
	}

	public class TalkDialogState extends SKState {

	}

	@Override
	protected void onHurt(int a, Creature from) {
		super.onHurt(a, from);

		if (from instanceof Player) {
			this.become("hana");
		}
	}
}