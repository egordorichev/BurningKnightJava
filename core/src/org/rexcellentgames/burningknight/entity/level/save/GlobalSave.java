package org.rexcellentgames.burningknight.entity.level.save;

import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GlobalSave {
  public static HashMap<String, String> values = new HashMap<>();

  public static boolean isTrue(String key) {
    String value = values.get(key);

    if (value == null) {
      return false;
    }

    return value.equals("true");
  }

  public static boolean isFalse(String key) {
    return !isTrue(key);
  }

  public static void put(String key, Object val) {
    values.put(key, val.toString());
  }

  public static int getInt(String key) {
    String value = values.get(key);

    if (value == null) {
      return 0;
    }

    return Integer.valueOf(value);
  }

  public static float getFloat(String key) {
    String value = values.get(key);

    if (value == null) {
      return 0;
    }

    return Float.valueOf(value);
  }

  public static void generate() {
    Settings.generate();
  }

  public static void load(FileReader reader) throws IOException {
    values.clear();

    int count = reader.readInt32();

    for (int i = 0; i < count; i++) {
      String key = reader.readString();
      String val = reader.readString();

      values.put(key, val);
    }
  }

  public static void save(FileWriter writer) throws IOException {
    writer.writeInt32(values.size());

    for (Map.Entry<String, String> pair: values.entrySet()) {
      writer.writeString(pair.getKey());
      writer.writeString(pair.getValue());
    }
  }
}