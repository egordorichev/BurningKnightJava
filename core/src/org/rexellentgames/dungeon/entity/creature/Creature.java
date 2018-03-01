package org.rexellentgames.dungeon.entity.creature;

import com.badlogic.gdx.physics.box2d.Body;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.fx.HpFx;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class Creature extends SaveableEntity {
	public static final float INV_TIME = 0.4f;
	public Point vel = new Point();
	protected int hp;
	protected int hpMax;
	protected float speed = 10;
	protected int damage = 2;
	protected int defense = 1;
	protected float invt = 0;
	protected boolean dead;
	protected Body body;
	protected String state = "idle";
	protected float t = 0;
	protected boolean flipped = false;
	protected ArrayList<Buff> buffs = new ArrayList<Buff>();
	protected float a = 1f;

	public void tp(float x, float y) {
		this.x = x;
		this.y = y;

		if (this.body != null) {
			this.body.setTransform(x, y, 0
			);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body.getWorld().destroyBody(this.body);
	}

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

		for (int i = this.buffs.size() - 1; i >= 0; i--) {
			this.buffs.get(i).update(dt);
		}
	}

	protected void common() {
		if (!this.dead) {
			if (this.vel.x < 0) {
				this.flipped = true;
			} else if (this.vel.x > 0) {
				this.flipped = false;
			}
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
			amount += this.defense;

			if (this.invt > 0) {
				return;
			}

			this.invt = INV_TIME;
			this.onHurt();
		}

		this.area.add(new HpFx(this, amount));
		this.hp = MathUtils.clamp(0, this.hpMax, this.hp + amount);

		if (this.hp == 0) {
			this.die();
		}
	}

	public boolean isDead() {
		return this.dead;
	}

	protected void onHurt() {

	}

	protected void die() {
		if (this.dead) {
			return;
		}

		Tween.to(new Tween.Task(0, 3f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}

			@Override
			public float function(float p) {
				return p;
			}

			@Override
			public void onEnd() {
				done = true;
			}
		});

		this.dead = true;
		Dungeon.level.removeSaveable(this);
	}

	protected void renderBuffs() {
		for (Buff buff : this.buffs) {
			buff.render(this);
		}
	}

	public int getHp() {
		return this.hp;
	}

	public int getHpMax() {
		return this.hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
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

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}
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
			buff.setOwner(this);
		}
	}

	public void removeBuff(Buff buff) {
		this.buffs.remove(buff);
	}

	public boolean isFlipped() {
		return this.flipped;
	}

	public ArrayList<Buff> getBuffs() {
		return this.buffs;
	}
}