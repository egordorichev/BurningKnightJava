package org.rexcellentgames.burningknight.entity.item.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.modifier.Modifier;
import org.rexcellentgames.burningknight.entity.pool.ModifierPool;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class WeaponBase extends Item {
	protected Modifier modifier;
	public int damage = 2;
	protected int minDamage = -1;
	public float timeA = 0.1f;
	public float timeDelay = 0f;
	protected float timeB = 0.1f;
	protected float knockback = 10f;
	public float critChance = 4f;
	public static boolean luck;
	public int initialDamage;
	public int initialDamageMin;
	public float initialCrit;

	@Override
	public int getPrice() {
		return 15;
	}

	public void modifyUseTime(float am) {
		this.useTime += am;

		if (this.timeB == 0) {
			this.timeA += am;
		} else {
			this.timeA += am / 2;
			this.timeB += am / 2;
		}
	}

	public void setCritChance(float c) {
		this.initialCrit = this.critChance;
		this.critChance = c;
	}

	public void resetCritChance() {
		this.critChance = initialCrit;
	}

	protected boolean lastCrit;

	public int rollDamage() {
		if (this.owner == null) {
			this.owner = Player.instance;
		}

		if (this.minDamage == -1) {
			minDamage = Math.round(((float) damage) / 3 * 2);
		}

		lastCrit = Random.chance(this.critChance + this.owner.getStat("crit_chance") * 10);
		return Math.round(Random.newFloatDice(this.minDamage, this.damage) * (lastCrit ? 2 : 1));
	}

	public void modifyDamage(int am) {
		this.initialDamage = damage;
		this.initialDamageMin = minDamage;

		this.damage += am;
		this.minDamage += am;
	}

	public void restoreDamage() {
		this.damage = initialDamage;
		this.minDamage = initialDamageMin;
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
		Mob.shader.begin();
		Mob.shader.setUniformf("u_color", new Vector3(r, g, b));
		Mob.shader.setUniformf("u_a", 1f);
		Mob.shader.end();
		Graphics.batch.setShader(Mob.shader);
		Graphics.batch.begin();
	}

	public void renderAt(float x, float y, float ox, float oy, float scale) {
		renderAt(x, y, 0, ox, oy, false, false, scale, scale);
	}

	public void renderAt(float x, float y, float a, float ox, float oy, boolean fx, boolean fy) {
		renderAt(x, y, a, ox, oy, fx, fy, 1, 1);
	}

	public static ShaderProgram shader;

	static {
		String vertexShader;
		String fragmentShader;
		vertexShader = Gdx.files.internal("shaders/blink.vert").readString();
		fragmentShader = Gdx.files.internal("shaders/blink.frag").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}


	public void renderAt(float x, float y, float a, float ox, float oy, boolean fx, boolean fy, float sx, float sy) {
		renderAt(x, y, a, ox, oy, fx, fy, sx, sy, 1);
	}

	public void renderAt(float x, float y, float a, float ox, float oy, boolean fx, boolean fy, float sx, float sy, float al) {
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

		Graphics.batch.end();
		shader.begin();
		shader.setUniformf("a", al == 1 ? (this.owner == null ? 1 : this.owner.a) : al);
		shader.setUniformf("time", Dungeon.time + this.t);
		shader.end();
		Graphics.batch.setShader(shader);
		Graphics.batch.begin();
		Graphics.render(getSprite(), x, y, a, ox, oy, fx, fy, sx, sy);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		if (this.useSpeedStr == null) {
			this.useSpeedStr = this.getUseSpeedAsString();
		}

		builder.append("\n[white]");
		builder.append(this.useSpeedStr);
		builder.append("[gray]");

		return builder;
	}

	private float t;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;
	}

	@Override
	public void init() {
		super.init();

		this.t = Random.newFloat(10f);
	}

	@Override
	public void onPickup() {
		super.onPickup();
		this.t = Random.newFloat(10f);
	}

	public void endRender() {
		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public void generate() {
		super.generate();

		if (Random.chance(25)) {
			this.generateModifier();
		}
	}

	public void generateModifier() {
		if (this.modifier == null) {
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