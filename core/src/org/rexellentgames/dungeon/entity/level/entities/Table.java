package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Table extends SolidProp {
	private boolean s;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.w = reader.readInt16();
		this.h = reader.readInt16();
	}

	private void makeBody() {
		this.body = World.createSimpleBody(this, 2, 4, this.w - 4, Math.max(1, this.h - 16 - 8), BodyDef.BodyType.StaticBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt16((short) this.w);
		writer.writeInt16((short) this.h);
	}

	@Override
	public void render() {
		if (!s) {
			s = true;

			for (int x = 0; x < this.w / 16; x++) {
				for (int y = 0; y < this.h / 16; y++) {
					Dungeon.level.setPassable((int) (x + this.x / 16), (int) (y + (this.y + 8) / 16), false);
				}
			}
		}

		if (this.body == null) {
			this.makeBody();
		}

		for (int x = 0; x < this.w / 16; x++) {
			for (int y = 0; y < this.h / 16; y++) {
				int count = 0;

				if (y < this.h / 16 - 1) {
					count += 1;
				}

				if (x < this.w / 16 - 1) {
					count += 2;
				}

				if (y > 0) {
					count += 4;
				}

				if (x > 0) {
					count += 8;
				}

				TextureRegion sprite = Terrain.tableVariants[count];
				Graphics.render(sprite, this.x + x * 16 + (16 - sprite.getRegionWidth()) / 2, this.y + y * 16 - 8 + (16 - sprite.getRegionHeight()) / 2);
			}
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h);
	}
}