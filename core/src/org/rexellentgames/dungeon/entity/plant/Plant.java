package org.rexellentgames.dungeon.entity.plant;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Plant extends SaveableEntity {
	protected int sprite;
	protected float growSpeed = 1f;
	protected float growProgress;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.growProgress < 1) {
			float a = Dungeon.level.getLight((int) this.x / 16, (int) this.y / 16);
			this.growProgress += this.growSpeed * a * dt * 0.05f;

			if (this.growProgress > 1) {
				this.growProgress = 1;
			}
		}
	}

	@Override
	public void render() {
		Graphics.render(Graphics.sprites, this.sprite + (int) Math.floor(this.growProgress * 2), this.x, this.y - 4);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeFloat(this.growProgress);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.growProgress = reader.readFloat();
	}
}