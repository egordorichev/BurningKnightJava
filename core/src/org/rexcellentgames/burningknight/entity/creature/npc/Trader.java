package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Trader extends Npc {
	public static ArrayList<Trader> all = new ArrayList<>();

	public boolean saved = false;
	public String id;

	{
		ignoreRooms = true;
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "hi": return new HiState();
			case "thanks": return new ThanksState();
		}

		return super.getAi(state);
	}

	public class TraderState extends Mob.State<Trader> {

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

		super.update(dt);
	}

	@Override
	public void render() {
		if (!this.saved && Dungeon.depth == -2) {
			return;
		}

		Graphics.render(Item.missing, this.x, this.y);
	}

	@Override
	public void renderShadow() {
		if (this.saved || Dungeon.depth != -2) {
			super.renderShadow();
		}
	}
}