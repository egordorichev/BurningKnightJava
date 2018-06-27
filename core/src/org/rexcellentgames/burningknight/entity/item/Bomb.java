
package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;

public class Bomb extends Item {
	{
		useTime = 1f;
		stackable = true;
		identified = true;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {

	}

	@Override
	public void use() {
		super.use();
		this.count -= 1;

		BombEntity e = new BombEntity(this.owner.x + (this.owner.w - 16) / 2, this.owner.y + (this.owner.h - 16) / 2).toMouseVel();
		e.owner = this.owner;

		Dungeon.area.add(e);


		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			if (player.fireBombs) {
				e.toApply.add(new BurningBuff());
			}

			if (player.iceBombs) {
				e.toApply.add(new BurningBuff());
			}

			if (player.poisonBombs) {
				e.toApply.add(new BurningBuff());
			}

			if (player.manaBombs) {
				player.modifyMana(player.getManaMax());
			}
		}
	}
}