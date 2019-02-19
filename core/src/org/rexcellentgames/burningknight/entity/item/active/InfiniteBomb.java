package org.rexcellentgames.burningknight.entity.item.active;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;

public class InfiniteBomb extends ActiveItem {
	{
		sprite = "item-bomb_orbital";
		stackable = false;
		useTime = 0.4f;
		name = Locale.get("infinite_bomb");
		description = Locale.get("infinite_bomb_desc");
	}

	@Override
	public void use() {
		if (this.delay > 0) {
			return;
		}

		super.use();

		BombEntity e = new BombEntity(this.owner.x + (this.owner.w - 16) / 2, this.owner.y + (this.owner.h - 16) / 2).toMouseVel();
		e.owner = this.owner;

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			e.leaveSmall = player.leaveSmall;
		}

		Dungeon.area.add(e);
	}
}