package org.rexellentgames.dungeon.entity.creature.mob.boss;

import org.rexellentgames.dungeon.assets.MusicManager;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

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
		MusicManager.highPriority("Reckless");
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

				if (self.canSee(player) && (!shouldBeInTheSameRoom || player.currentRoom == self.room)) {
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