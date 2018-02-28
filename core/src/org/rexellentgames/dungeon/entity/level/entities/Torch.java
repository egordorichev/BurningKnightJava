package org.rexellentgames.dungeon.entity.level.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.creature.buff.fx.FlameFx;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;

import java.io.IOException;

public class Torch extends SaveableEntity {
	private float t;
	private PointLight light;

	public Torch(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Torch() {

	}

	@Override
	public void init() {
		this.light = new PointLight(this.area.getState().getLight(), 128, new Color(1, 0.7f, 0.5f, 0.9f),
			96, this.x + 8, this.y);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.light.setPosition(this.x + 8, this.y);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.light.remove(true);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.light.setDistance((float) (Math.cos(this.t * 5) * 16 + 96));
		this.t += dt;

		if (this.t % 0.1 <= 0.017) {
			this.area.add(new FlameFx(this));
		}
	}
}