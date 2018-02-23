package org.rexellentgames.dungeon.entity.creature;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Creature extends SaveableEntity {
	protected int hp;
	protected int hpMax;
	protected boolean dead;
	protected Body body;
	protected String state = "idle";
	protected float t = 0;

	@Override
	public void init() {
		super.init();

		this.hp = this.hpMax;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (this.body != null) {
			this.x = this.body.getPosition().x;
			this.y = this.body.getPosition().y;
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

		this.hp = MathUtils.clamp(0, this.hpMax, this.hp + amount);

		if (this.hp == 0) {
			this.die();
		}
	}

	protected void die() {
		this.dead = true;
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
}