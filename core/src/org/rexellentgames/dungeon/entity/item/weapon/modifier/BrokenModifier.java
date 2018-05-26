package org.rexellentgames.dungeon.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;

public class BrokenModifier extends Modifier {
	public BrokenModifier() {
		super("Broken");
	}

	private static Color color = Color.valueOf("#ed4f6f");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.modifyDamage(-1);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.modifyDamage(1);
	}

	@Override
	public void apply(StringBuilder builder) {
		super.apply(builder);

		builder.append("[red]-1 damage[gray]");
	}
}