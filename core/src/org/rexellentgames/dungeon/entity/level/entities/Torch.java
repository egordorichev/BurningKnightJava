package org.rexellentgames.dungeon.entity.level.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.buff.fx.FlameFx;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;

import java.io.IOException;

public class Torch extends SaveableEntity {
	private float t;
	private PointLight light;

	public Torch(float x, float y) {
		this.x = x;
		this.y = y;
		this.t = Random.newFloat(1024);
	}

	public Torch() {

	}

	@Override
	public void init() {
		if (Dungeon.light != null) {
			this.light = new PointLight(Dungeon.light, 128, new Color(1, 0.7f, 0.5f, 0.9f),
				96, this.x + 8, this.y);
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		if (Dungeon.light != null) {
			this.light.setPosition(this.x + 8, this.y);
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		if (Dungeon.light != null) {
			this.light.remove(true);
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.light != null) {
			this.light.setDistance((float) (Math.cos(this.t * 5) * 16 + 96));
		}

		this.t += dt;

		if (this.t % 0.1 <= 0.017) {
			this.area.add(new FlameFx(this));
		}
	}
}