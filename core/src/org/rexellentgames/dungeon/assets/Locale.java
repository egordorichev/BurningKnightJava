package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexellentgames.dungeon.util.Log;

import java.util.HashMap;

public class Locale {
	private static HashMap<String, String> map = new HashMap<>();

	public static void load(String locale) {
		map.clear();

		JsonReader reader = new JsonReader();
		JsonValue root = reader.parse(Gdx.files.internal("locales/" + locale + ".json"));

		for (JsonValue value : root) {
			map.put(value.name, value.asString());
		}
	}

	public static boolean has(String name) {
		return map.containsKey(name);
	}

	public static String get(String name) {
		String string = map.get(name);

		if (name == null) {
			return "Missing Locale";
		}

		return string;
	}
}