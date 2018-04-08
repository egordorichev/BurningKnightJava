package org.rexellentgames.dungeon.entity.item.weapon.magic;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.buff.InvisibilityBuff;
import org.rexellentgames.dungeon.entity.creature.player.GhostPlayer;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class GhostLeaver extends MagicWeapon {
	{
		name = "Ghost Leaver";
		description = "Ghosts are real";
		sprite = "item (scroll C)";
		damage = 6;
		mana = 100;
		useTime = 10f;
		stackable = true;
	}

	@Override
	public void use() {
		super.use();

		Player.instance.addBuff(new InvisibilityBuff().setDuration(1000000000));
		this.setCount(this.getCount() - 1);

		Player player = new GhostPlayer();
		Dungeon.area.add(player);
	}
}