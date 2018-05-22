package org.rexellentgames.dungeon.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
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
		return null;
	}

	public void apply(WeaponBase weapon) {
	  weapon.setModifier(this);
	}

	public void remove(WeaponBase weapon) {
		weapon.setModifier(null);
	}
}