package org.rexcellentgames.burningknight.entity.level.entities;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Chair extends Prop {
	public boolean flipped;

	@Override
	public void init() {
		this.sprite = flipped ? "props-chair_a" : "props-char_b";
		super.init();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		region = Graphics.getTexture(this.sprite);
		flipped = reader.readBoolean();
		this.sprite = flipped ? "props-chair_a" : "props-chair_b";
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(flipped);
	}
}