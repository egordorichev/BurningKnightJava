package org.rexcellentgames.burningknight.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;

public class Modifier {
	public int id;
	protected String name;

	public String getName() {
		return name;
	}

	public Modifier(String name) {
		this.name = name;
	}

	public Color getColor() {
		return Color.WHITE;
	}

	public void apply(WeaponBase weapon) {

	}

	public void remove(WeaponBase weapon) {

	}

	public void onHit(Player owner, Creature creature, int damage) {

	}

	public int modDamage(int damage) {
		return damage;
	}

	public void apply(StringBuilder builder) {
		builder.append("\n\n");
	}
}