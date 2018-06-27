package org.rexcellentgames.burningknight.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;

public class FastModifier extends Modifier {
	public FastModifier() {
		super("Fast");
	}

	private static Color color = Color.valueOf("#51ddb0");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.modifyUseTime(-0.1f);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.modifyUseTime(0.1f);
	}

	@Override
	public void apply(StringBuilder builder) {
		super.apply(builder);

		builder.append("[green]-0.1 use time[gray]");
	}
}