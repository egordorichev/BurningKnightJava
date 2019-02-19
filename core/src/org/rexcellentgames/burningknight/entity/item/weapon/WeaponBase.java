package org.rexcellentgames.burningknight.entity.item.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.util.Random;

public class WeaponBase extends Item {
	public static ShaderProgram shader;
	private static String critLocale = Locale.get("crit_chance");
	private static String damageLocale = Locale.get("damage");
	private static String penetratesLocale = Locale.get("penetrates");

	static {
		String vertexShader;
		String fragmentShader;
		vertexShader = Gdx.files.internal("shaders/default.vert").readString();
		fragmentShader = Gdx.files.internal("shaders/blink.frag").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	public int damage = 2;
	public float timeA = 0.1f;
	public float timeDelay = 0f;
	public int initialDamage;
	public int initialDamageMin;
	protected int minDamage = -1;
	protected float timeB = 0.1f;
	protected float knockback = 10f;
	protected boolean penetrates;
	private float t;

	protected String getSfx() {
		return "whoosh";
	}

	@Override
	public int getPrice() {
		return 15;
	}

	@Override
	public void onPickup() {
		super.onPickup();
		this.t = Random.newFloat(10f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.t += dt;
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		if (this.minDamage == -1) {
			minDamage = Math.round(((float) damage) / 3 * 2);
		}

		builder.append("\n[orange]");

		float mod = Player.instance.getDamageModifier();

		int min = Math.max(1, Math.round((this.minDamage) * mod));
		int dmg = Math.max(1, Math.round((this.damage) * mod));

		if (min != dmg) {
			builder.append(min);
			builder.append("-");
		}

		builder.append(dmg);
		builder.append(" ");
		builder.append(damageLocale);
		builder.append("[gray]");

		if (this.penetrates || this.owner.penetrates) {
			builder.append("\n[green]");
			builder.append(penetratesLocale);
			builder.append("[gray]");
		}

		return builder;
	}

	public int rollDamage() {
		if (this.owner == null) {
			this.owner = Player.instance;
		}

		if (this.minDamage == -1) {
			minDamage = Math.round(((float) damage) / 3 * 2);
		}

		if (this.owner.isTouching(Terrain.ICE)) {
			return this.damage;
		}

		return Math.round(Random.newFloatDice(this.minDamage, this.damage));
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

	public void renderAt(float x, float y, float ox, float oy, float scale) {
		renderAt(x, y, 0, ox, oy, false, false, scale, scale);
	}

	public void renderAt(float x, float y, float a, float ox, float oy, boolean fx, boolean fy, float sx, float sy) {
		renderAt(x, y, a, ox, oy, fx, fy, sx, sy, 1, 1);
	}

	public void renderAt(float x, float y, float a, float ox, float oy, boolean fx, boolean fy, float sx, float sy, float al, float gray) {
		Graphics.batch.setColor(1, 1, 1, 1);

		Graphics.batch.end();
		shader.begin();
		shader.setUniformf("gray", gray);
		shader.setUniformf("a", al == 1 ? (this.owner == null ? 1 : this.owner.a) : al);
		shader.setUniformf("time", this.t);
		shader.end();
		Graphics.batch.setShader(shader);
		Graphics.batch.begin();
		Graphics.render(getSprite(), x, y, a, ox, oy, fx, fy, sx, sy);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	public void renderAt(float x, float y, float a, float ox, float oy, boolean fx, boolean fy) {
		renderAt(x, y, a, ox, oy, fx, fy, 1, 1);
	}

	@Override
	public void init() {
		super.init();
		this.t = Random.newFloat(10f);
	}
}