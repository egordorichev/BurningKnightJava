package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;

public class GoreFx extends SaveableEntity {
	public TextureRegion texture;
	private float a;
	private float va;
	private Point vel;
	private float z = 8;
	public boolean added;

	@Override
	public void init() {
		super.init();

		if (this.added) {
			this.a = Random.newFloat(360);
			this.va = Random.newFloat(-30f, 30f);

			this.vel = new Point(Random.newFloat(-2f, 2f), 1f);
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.a = reader.readFloat();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeFloat(this.a);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x;
		this.z = Math.max(0, this.z + this.vel.y);

		this.vel.y -= 0.05;

		this.a += this.va;
		this.va *= 0.95f;

		this.vel.x *= 0.95f;
	}

	@Override
	public void render() {
		Graphics.startShadows();
		Graphics.render(this.texture, this.x, this.y, this.a, this.texture.getRegionWidth() / 2, this.texture.getRegionHeight() / 2, false, false, 1f, -1f);
		Graphics.endShadows();
		Graphics.render(this.texture, this.x, this.y + this.z, this.a, this.texture.getRegionWidth() / 2, this.texture.getRegionHeight() / 2, false, false);
	}
}