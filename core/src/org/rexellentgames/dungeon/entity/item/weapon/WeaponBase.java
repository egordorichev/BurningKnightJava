package org.rexellentgames.dungeon.entity.item.weapon;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.pool.ModifierPool;
import org.rexellentgames.dungeon.entity.item.weapon.modifier.Modifier;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class WeaponBase extends Item {
	protected Modifier modifier;
	protected int damage = 1;
	protected int minDamage = 1;
	protected float knockback = 10f;

	public void modifyUseTime(float am) {
		this.useTime += am;
	}

	public void modifyDamage(int am) {
		this.damage += am;
	}

	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}

	public Modifier getModifier() {
		return this.modifier;
	}

	public void applyColor() {
		Modifier modifier = this.getModifier();

		float r = 1f;
		float g = 1f;
		float b = 1f;

		if (modifier != null) {
			float a = (float) Math.abs(Math.sin(Dungeon.time));

			r -= (1 - modifier.getColor().r) * a;
			g -= (1 - modifier.getColor().g) * a;
			b -= (1 - modifier.getColor().b) * a;
		}

		Graphics.batch.setColor(r, g, b, this.a);
	}


	@Override
	public void generate() {
		super.generate();

		if (Random.chance(50)) {
			this.setModifier(ModifierPool.instance.generate());
		}
	}

	@Override
	public String getName() {
		if (this.modifier != null) {
			return this.modifier.getName() + " " + super.getName();
		}

		return super.getName();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		int id = reader.readInt16();

		if (id > 0) {
			this.setModifier(ModifierPool.instance.getModifier(id - 1));
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		if (this.modifier == null) {
			writer.writeInt16((short) 0);
		} else {
			writer.writeInt16((short) (this.modifier.id + 1));
		}
	}
}