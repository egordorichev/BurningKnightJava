package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.entity.NetworkedEntity;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class SaveableEntity extends NetworkedEntity {
	public void save(FileWriter writer) throws IOException {
		writer.writeInt32((int) this.x);
		writer.writeInt32((int) this.y);
	}

	public void load(FileReader reader) throws IOException {
		this.x = reader.readInt32();
		this.y = reader.readInt32();
	}

	public SaveableEntity add() {
		LevelSave.all.add(this);
		return this;
	}

	public SaveableEntity remove() {
		LevelSave.all.remove(this);
		return this;
	}
}