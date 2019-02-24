package org.rexcellentgames.burningknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public class Locale {
	private static HashMap<String, String> map = new HashMap<>();
	private static HashMap<String, String> fallback = new HashMap<>();
	public static String current;

	private static void loadRaw(String json, boolean fl) {
		String js = json.replaceAll("(\"\\{\")", "\\{\"").replaceAll("(\"}\")","\"\\}");

		JsonReader reader = new JsonReader();
		JsonValue root = reader.parse(js);

		for (JsonValue value : root) {
			if (fl) {
				fallback.put(value.name, value.asString());
			} else {
				map.put(value.name, value.asString());
			}
		}
	}

	public static void load(String locale) {
		current = locale;

		map.clear();
		loadRaw(Gdx.files.internal(String.format("locales/%s.json", locale)).readString(), false);

		if (!locale.equals("en")) {
			loadRaw(Gdx.files.internal("locales/en.json").readString(), true);
		}
	}

	public static boolean has(String name) {
		return map.containsKey(name) || fallback.containsKey(name);
	}

	public static String get(String name) {
		return map.containsKey(name) ? map.get(name) : fallback.getOrDefault(name, name);
	}
}