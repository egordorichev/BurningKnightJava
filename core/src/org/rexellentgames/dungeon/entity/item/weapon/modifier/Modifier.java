package org.rexellentgames.dungeon.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;

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
	  weapon.setModifier(this);
	}

	public void remove(WeaponBase weapon) {
		weapon.setModifier(null);
	}

	public void onHit(Player owner, Creature creature, int damage) {

	}

	public int modDamage(int damage) {
		return damage;
	}
}