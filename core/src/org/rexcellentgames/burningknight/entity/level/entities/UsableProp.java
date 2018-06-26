package org.rexcellentgames.burningknight.entity.level.entities;

import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class UsableProp extends SolidProp {
  protected boolean used;

  public boolean use() {
    return true;
  }

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