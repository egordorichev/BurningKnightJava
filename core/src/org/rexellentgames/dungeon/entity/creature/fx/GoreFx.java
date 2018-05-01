package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;


public class GoreFx extends Entity {
	public TextureRegion texture;
	public boolean menu;
	private float a;
	private float va;
	private Point vel;
	private float z = 8;

	@Override
	public void init() {
		super.init();

		this.depth = -1;
		this.a = Random.newFloat(360);
		this.va = Random.newFloat(-30f, 30f);

		this.vel = new Point(Random.newFloat(-4f, 4f), 1f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x;

		if (!menu) {
			this.z = Math.max(0, this.z + this.vel.y);
		} else {
			this.z += this.vel.y;
			if (this.y + this.z < 0) {
				this.done = true;
			}
		}

		this.vel.y -= 0.05;

		this.a += this.va;
		this.va *= 0.95f;

		this.vel.x *= 0.95f;
	}

	@Override
	public void render() {
		if (!menu) {
			Graphics.startShadows();
			Graphics.render(this.texture, this.x, this.y - 4, this.a, this.texture.getRegionWidth() / 2, this.texture.getRegionHeight() / 2, false, false, 1f, -1f);
			Graphics.endShadows();
		}

		Graphics.render(this.texture, this.x, this.y + this.z, this.a, this.texture.getRegionWidth() / 2, this.texture.getRegionHeight() / 2, false, false);
	}
}