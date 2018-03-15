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
	public void destroy() {
		super.destroy(); }

	@Override
	public void update(float dt) {
		super.update(dt);

		Dungeon.level.addLightInRadius(this.x + 16, this.y + 16, 0, 0, 0.0f, 0.5f, 3f, false);
		// todo: move with time

		this.t += dt;

		if (this.t % 0.1 <= 0.017) {
			this.area.add(new FlameFx(this));
		}
	}
}