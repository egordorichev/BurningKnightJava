package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
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
	protected float timeA = 0.1f;
	protected float timeB = 0.1f;
	protected float knockback = 10f;
	protected float critChance = 4f;

	public void modifyUseTime(float am) {
		this.useTime += am;
		this.timeA += am / 2;
		this.timeB += am / 2;
	}

	protected boolean lastCrit;

	public int rollDamage() {
		lastCrit = Random.chance(this.critChance + this.owner.critChance);
		return Math.round(Random.newFloatDice(this.minDamage, this.damage) * (lastCrit ? 2 : 1));
	}

	public void modifyDamage(int am) {
		this.damage += am;
		this.minDamage += am;
	}

	public void setModifier(Modifier modifier) {
		if (this.modifier != null) {
			this.modifier.remove(this);
		}

		this.modifier = modifier;

		if (this.modifier != null) {
			this.modifier.apply(this);
		}
	}

	public void startRender() {
		float a = (float) Math.abs(Math.sin(Dungeon.time));
		Color c = modifier.getColor();

		float r = 1f - (1 - c.r) * a;
		float g = 1f - (1 - c.g) * a;
		float b = 1f - (1 - c.b) * a;


		Graphics.batch.end();
		Mob.shaderOutline.begin();
		Mob.shaderOutline.setUniformf("u_color", new Vector3(r, g, b));
		Mob.shaderOutline.end();
		Graphics.batch.setShader(Mob.shaderOutline);
		Graphics.batch.begin();
	}

	public void renderAt(float x, float y, float ox, float oy, float scale) {
		renderAt(x, y, 0, ox, oy, false, false, scale, scale);
	}

	public void renderAt(float x, float y, float a, float ox, float oy, boolean fx, boolean fy) {
		renderAt(x, y, a, ox, oy, fx, fy, 1, 1);
	}

	public void renderAt(float x, float y, float a, float ox, float oy, boolean fx, boolean fy, float sx, float sy) {
		Graphics.batch.setColor(1, 1, 1, 1);

		if (this.modifier != null) {
			startRender();

			for (int xx = -1; xx < 2; xx++) {
				for (int yy = -1; yy < 2; yy++) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						Graphics.render(getSprite(), x, y, a, ox + xx, oy + yy, fx, fy, sx, sy);
					}
				}
			}

			endRender();
		}

		Graphics.render(getSprite(), x, y, a, ox, oy, fx, fy, sx, sy);
	}

	public void endRender() {
		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
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