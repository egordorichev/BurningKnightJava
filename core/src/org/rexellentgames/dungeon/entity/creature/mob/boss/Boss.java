package org.rexellentgames.dungeon.entity.creature.mob.boss;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;

import java.util.ArrayList;

public class Boss extends Mob {
	public static ArrayList<Boss> all = new ArrayList<>();
	public String texture;

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

	public class BossState<T> extends State {
		public void checkForTarget() {
			if (self.target != null && !self.getState().equals("roam") && !self.getState().equals("idle")) {
				return;
			}

			for (Creature player : Player.all) {
				if (player.invisible) {
					continue;
				}

				if (self.canSee(player)) {
					self.target = player;
					self.become("alerted");
					self.noticeSignT = 2f;
					self.hideSignT = 0f;

					return;
				}
			}
		}

		@Override
		public void checkForFlee() {

		}
	}
}