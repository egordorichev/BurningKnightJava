package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Log;

public class BossThief extends Thief {
	public static Animation animations = Animation.make("actor-thief", "-green");

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 20;
	}

	@Override
	public void onHit(Creature who) {
		super.onHit(who);

		if (stolen == null && who instanceof Player) {
			Player player = (Player) who;

			for (int i = 0; i < 6; i++) {
				if (i != player.getInventory().active) {
					Item item = player.getInventory().getSlot(i);

					if (item != null) {
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
}