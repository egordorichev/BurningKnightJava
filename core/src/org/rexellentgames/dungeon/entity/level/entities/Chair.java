package org.rexellentgames.dungeon.entity.level.entities;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Chair extends Prop {
	public boolean flipped;

	@Override
	public void init() {
		this.sprite = flipped ? "biome-0 (chair A)" : "biome-0 (chair B)";
		super.init();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.sprite = flipped ? "biome-0 (chair A)" : "biome-0 (chair B)";
		region = Graphics.getTexture(this.sprite);
		flipped = reader.readBoolean();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(flipped);
	}
}