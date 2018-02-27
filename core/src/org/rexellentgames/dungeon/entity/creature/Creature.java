package org.rexellentgames.dungeon.entity.creature;

import com.badlogic.gdx.physics.box2d.Body;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.fx.HpFx;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class Creature extends SaveableEntity {
	public static final float INV_TIME = 0.25f;

	// Stats
	protected int hp;
	protected int hpMax;
	protected float speed = 10;
	protected int damage = 1;
	protected int defense = 1;
	protected float invt = 0;

	protected boolean dead;
	protected Body body;
	protected String state = "idle";
	protected float t = 0;
	protected boolean flipped = false;
	protected Point vel = new Point();
	protected ArrayList<Buff> buffs = new ArrayList<Buff>();

	@Override
	public void init() {
		super.init();

		this.t = Random.newFloat(1024);
		this.hp = this.hpMax;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;
		this.invt = Math.max(0, this.invt - dt);

		if (this.body != null) {
			this.x = this.body.getPosition().x;
			this.y = this.body.getPosition().y;
		}
	}

	protected void common() {
		if (this.vel.x < 0) {
			this.flipped = true;
		} else if (this.vel.x > 0) {
			this.flipped = false;
		}

		if (this.body != null) {
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
		}
	}

	public void become(String state) {
		if (!this.state.equals(state)) {
			this.state = state;
			this.t = 0;
		}
	}

	public void modifyHp(int amount) {
		if (this.dead) {
			return;
		}

		if (amount < 0) {
			amount -= this.defense;

			if (this.invt > 0) {
				return;
			}

			this.invt = INV_TIME;
		}

		this.area.add(new HpFx(this, amount));
		this.hp = MathUtils.clamp(0, this.hpMax, this.hp + amount);

		if (this.hp == 0) {
			this.die();
		}
	}

	protected void die() {
		this.dead = true;
		this.done = true;
	}

	public int getHp() {
		return this.hp;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getHpMax() {
		return this.hpMax;
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt32(this.hp);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.hp = reader.readInt32();
	}

	public Body getBody() {
		return this.body;
	}

	protected boolean canHaveBuff(Buff buff) {
		return true;
	}

	public void addBuff(Buff buff) {
		if (this.canHaveBuff(buff)) {
			this.buffs.add(buff);
		}
	}

	public boolean isFlipped() {
		return this.flipped;
	}
}