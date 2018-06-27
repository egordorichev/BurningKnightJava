package org.rexcellentgames.burningknight.entity.item.consumable.spell;

import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;

import java.util.ArrayList;

public class SpellOfDamage extends Spell {
	{
		name = Locale.get("damage_spell");
		description = Locale.get("damage_spell_desc");
	}

	@Override
	public void use() {
		super.use();

		ArrayList<Entity> entities = Dungeon.area.getEntities();

		for (int i = entities.size() - 1; i >= 0; i--) {
			Entity entity = entities.get(i);

			if (entity instanceof Creature) {
				((Creature) entity).modifyHp(-20, null, true);
			}
		}
	}
}