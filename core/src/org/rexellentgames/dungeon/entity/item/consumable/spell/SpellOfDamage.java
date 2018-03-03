package org.rexellentgames.dungeon.entity.item.consumable.spell;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;

import java.util.ArrayList;

public class SpellOfDamage extends Spell {
	{
		name = "Spell of Damage";
		description = "It hurts";
	}

	@Override
	public void use() {
		super.use();

		ArrayList<Entity> entities = Dungeon.area.getEntities();

		for (int i = entities.size() - 1; i >= 0; i--) {
			Entity entity = entities.get(i);

			if (entity instanceof Creature) {
				((Creature) entity).modifyHp(-20, true);
			}
		}
	}
}