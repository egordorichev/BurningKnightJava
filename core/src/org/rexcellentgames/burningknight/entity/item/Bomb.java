
package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.util.Random;

public class Bomb extends Item {
	{
		name = Locale.get("bomb");
		description = Locale.get("bomb_desc");
		sprite = "item-bomb";
		useTime = 0.3f;
		stackable = true;
		autoPickup = true;
	}

	@Override
	public void generate() {
		super.generate();
		setCount(Random.newInt(3, 8));
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {

	}

	@Override
	public void use() {
		if (this.delay > 0) {
			return;
		}

		super.use();

		this.count -= 1;

		BombEntity e = new BombEntity(this.owner.x + (this.owner.w - 16) / 2, this.owner.y + (this.owner.h - 16) / 2).toMouseVel();
		e.owner = this.owner;

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			e.leaveSmall = player.leaveSmall;

			if (player.fireBombs) {
				e.toApply.add(new BurningBuff());
			}

			if (player.iceBombs) {
				e.toApply.add(new FreezeBuff());
			}

			if (player.poisonBombs) {
				e.toApply.add(new PoisonBuff());
			}

			if (player.manaBombs) {
				player.modifyMana(player.getManaMax());
			}
		}

		Dungeon.area.add(e);
	}
}