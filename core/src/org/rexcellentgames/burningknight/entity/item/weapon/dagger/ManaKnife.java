package org.rexcellentgames.burningknight.entity.item.weapon.dagger;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ManaKnife extends Dagger {
	{
		description = Locale.get("mana_knife_desc");
		name = Locale.get("mana_knife");
		sprite = "item-mana_knife";
		damage = 1;
	}

	@Override
	protected void setStats() {
		description = Locale.get("mana_knife_desc");
		name = Locale.get("mana_knife");
		sprite = "item-mana_knife";
		damage = 1;
		region = Graphics.getTexture(sprite);
	}

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);

		if (this.owner instanceof Player) {
			((Player) this.owner).modifyMana(1);
		}
	}
}