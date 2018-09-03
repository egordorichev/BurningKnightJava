package org.rexcellentgames.burningknight.entity.creature.mob.boss;

import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.entities.Coin;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Boss extends Mob {
	public static ArrayList<Boss> all = new ArrayList<>();
	public String texture;
	public boolean ignoreHealthbar;
	public boolean shouldBeInTheSameRoom;
	public boolean talked;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		talked = reader.readBoolean();
		this.shouldBeInTheSameRoom = !this.talked;
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(talked);
	}

	@Override
	public void init() {
		super.init();

		all.add(this);
	}

	@Override
	public void destroy() {
		super.destroy();

		all.remove(this);
	}

	@Override
	protected void die(boolean force) {
		super.die(force);
		Audio.highPriority("Reckless");
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> drops = super.getDrops();

		for (int i = 0; i < Random.newInt(5, 16); i++) {
			drops.add(new Coin());
		}

		return drops;
	}

	public class BossState<T extends Mob> extends State<T> {
		public void checkForTarget() {
			if (self.target != null && !self.getState().equals("roam") && !self.getState().equals("idle")) {
				return;
			}

			for (Player player : Player.all) {
				if (player.invisible) {
					continue;
				}

				if (self.canSee(player) && (!shouldBeInTheSameRoom || player.room == self.room)) {
					self.target = player;
					self.become("alerted");
					self.noticeSignT = 2f;
					self.hideSignT = 0f;

					return;
				}
			}
		}
	}
}