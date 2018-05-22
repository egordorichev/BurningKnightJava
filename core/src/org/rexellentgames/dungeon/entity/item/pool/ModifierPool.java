package org.rexellentgames.dungeon.entity.item.pool;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.item.weapon.modifier.Modifier;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class ModifierPool {
	public static ModifierPool instance = new ModifierPool();

	public ModifierPool() {
		add(new Modifier("Blazing") {
			@Override
			public Color getColor() {
				return new Color(1, 0, 0, 0);
			}
		}, 1f);
	}

	public Modifier getModifier(int id) {
		return classes.get(id);
	}

	protected ArrayList<Modifier> classes = new ArrayList<>();
	protected ArrayList<Float> chances = new ArrayList<>();

	public Modifier generate() {
		return classes.get(Random.chances(chances.toArray(new Float[0])));
	}

	protected void add(Modifier type, float chance) {
		type.id = this.classes.size();

		classes.add(type);
		chances.add(chance);
	}
}