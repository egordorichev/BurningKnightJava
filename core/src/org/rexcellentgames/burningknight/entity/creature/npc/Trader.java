package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Trader extends Npc {
	public static ArrayList<Trader> all = new ArrayList<>();
	private AnimationData animation;

	public boolean saved = false;
	public String id;

	{
		ignoreRooms = true;
	}

	public class TraderState extends Mob.State<Trader> {

	}

	public class IdleState extends TraderState {

	}

	@Override
	protected State getAi(String state) {
		return new IdleState();

		/*
		switch (state) {
			//case "hi": return new HiState();
			//case "thanks": return new ThanksState();
		}*/
	}

	private static String[] dialogs = {
		"hi", "hey", "how_is_it_going"
	};

	public class HiState extends TraderState {
		private NpcDialog dialog;
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(5f, 7f);

			dialog = new NpcDialog(self, Locale.get(dialogs[Random.newInt(dialogs.length - 1)]));
			dialog.open();

			Dungeon.area.add(dialog);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= this.delay) {
				self.become("idle");
			}
		}

		@Override
		public void onExit() {
			super.onExit();
			dialog.remove();
		}
	}

	public class ThanksState extends TraderState {
		private NpcDialog dialog;
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(5f, 7f);

			dialog = new NpcDialog(self, Locale.get("thanks_for_saving"));
			dialog.open();

			Dungeon.area.add(dialog);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= this.delay) {
				if (this.third) {
					self.become("idle");
				} else {
					this.second = true;
					delay = Random.newFloat(5f, 7f);
					this.t = 0;
					dialog.remove();
				}
			} else if (!this.third && this.second) {
				if (this.t >= 1f) {
					dialog = new NpcDialog(self, Locale.get("see_you_in_the_asylum"));
					dialog.open();
					this.third = true;
					Dungeon.area.add(dialog);
				}
			}
		}

		private boolean second;
		private boolean third;

		@Override
		public void onExit() {
			super.onExit();
			dialog.remove();
		}
	}

	@Override
	public void init() {
		super.init();
		all.add(this);

		if (this.id != null) {
			this.saved = this.id.equals("b") || this.id.equals("f") || GlobalSave.isTrue("npc_" + this.id + "_saved");
			this.loadSprite();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		all.remove(this);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(this.saved);
		writer.writeString(this.id);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.saved = reader.readBoolean();
		this.id = reader.readString();

		if (this.id != null) {
			this.saved = this.id.equals("b") || this.id.equals("f") || GlobalSave.isTrue("npc_" + this.id + "_saved");
			this.loadSprite();
		}
	}

	private void loadSprite() {
		if (this.id.equals("c")) {
			this.animation = Animation.make("actor-npc-consumables-trader").get("idle");
		} else if (this.id.equals("a")) {
			this.animation = Animation.make("actor-npc-accessory-trader").get("idle");
		} else if (this.id.equals("b")) {
			this.animation = Animation.make("actor-npc-permanent-upgrades-trader").get("idle");
		} else if (this.id.equals("d")) {
			this.animation = Animation.make("actor-npc-weapons-trader").get("idle");
		} else if (this.id.equals("h")) {
			this.animation = Animation.make("actor-npc-hats-trader").get("idle");
		}


		if (this.animation != null) {
			w = animation.getFrames().get(0).frame.getRegionWidth();
			h = animation.getFrames().get(0).frame.getRegionHeight();
		}
	}

	@Override
	public void update(float dt) {
		if (!this.saved && Dungeon.depth == -2) {
			return;
		}

		if (Dungeon.depth != -2 && this.saved && !this.onScreen) {
			this.done = true;
			this.remove();
		}

		if (this.animation != null) {
			this.animation.update(dt);
		}

		super.update(dt);
	}

	@Override
	public void render() {
		if (!this.saved && Dungeon.depth == -2) {
			return;
		}

		if (this.animation != null) {
			Graphics.render(this.animation.getCurrent().frame, this.x, this.y);
		} else {
			Graphics.render(Item.missing, this.x, this.y);
		}
	}

	@Override
	public void renderShadow() {
		if (this.saved || Dungeon.depth != -2) {
			super.renderShadow();
		}
	}
}