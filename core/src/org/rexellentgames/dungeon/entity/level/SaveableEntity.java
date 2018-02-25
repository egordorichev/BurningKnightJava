package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class SaveableEntity extends Entity {
	protected RegularLevel level;

	public void save(FileWriter writer) throws IOException {
		writer.writeInt32((int) this.x);
		writer.writeInt32((int) this.y);
	}

	public void load(FileReader reader) throws IOException {
		this.x = reader.readInt32();
		this.y = reader.readInt32();
	}

	public void setLevel(RegularLevel level) {
		this.level = level;
	}

	public RegularLevel getLevel() {
		return this.level;
	}
}