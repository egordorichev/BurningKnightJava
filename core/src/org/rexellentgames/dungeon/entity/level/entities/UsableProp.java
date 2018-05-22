package org.rexellentgames.dungeon.entity.level.entities;

import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class UsableProp extends SolidProp {
	public boolean use() {
		return true;
	}

	protected boolean used;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		used = reader.readBoolean();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.used);
	}
}