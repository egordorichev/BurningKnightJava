package org.rexcellentgames.burningknight.entity.creature.player;

import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Rect;

import java.io.IOException;

public class Spawn extends SaveableEntity {
	public static Spawn instance;
	public Rect room = new Rect();

	@Override
	public void init() {
		super.init();
		instance = this;
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		room.left = reader.readInt16();
		room.top = reader.readInt16();
		room.resize(reader.readInt16(), reader.readInt16());
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt16((short) room.left);
		writer.writeInt16((short) room.top);
		writer.writeInt16((short) room.getWidth());
		writer.writeInt16((short) room.getHeight());
	}

	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}
}