package org.rexcellentgames.burningknight.entity.level.entities.decor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Cobweb extends SaveableEntity {
	public static TextureRegion textures[] = new TextureRegion[] {
		Graphics.getTexture("props-cobweb_top_left"),
		Graphics.getTexture("props-cobweb_top_right"),
		Graphics.getTexture("props-cobweb_bottom_left"),
		Graphics.getTexture("props-cobweb_bottom_right")
	};

	{
		depth = 4;
	}

	public int side;

	@Override
	public void render() {
		Graphics.render(textures[this.side], this.x, this.y);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeByte((byte) side);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		side = reader.readByte();
	}
}