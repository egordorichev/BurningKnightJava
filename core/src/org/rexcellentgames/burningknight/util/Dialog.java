package org.rexcellentgames.burningknight.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexcellentgames.burningknight.assets.Locale;

import java.util.HashMap;

public class Dialog {
	public static DialogData active;
	private HashMap<String, DialogData> data = new HashMap<>();

	public static Dialog make(String file) {
		JsonReader reader = new JsonReader();
		JsonValue root = reader.parse(Gdx.files.internal("dialogs/" + file + ".json"));

		Dialog dialog = new Dialog();

		for (JsonValue part : root) {
			DialogData dt = new DialogData();

			for (JsonValue value : part) {
				Phrase phrase = new Phrase();

				phrase.string = Locale.get(value.name);
				phrase.name = value.name;
				// phrase.region = Graphics.getTexture(value.getString("texture"));
				phrase.owner = Locale.get(value.getString("name"));

				JsonValue options = value.get("options");

				if (options != null) {
					if (options.isArray()) {
						String[] array = options.asStringArray();
						phrase.options = new Option[array.length];

						for (int i = 0; i < array.length; i++) {
							phrase.options[i] = new Option(Locale.get(array[i]));
						}
					} else {
						phrase.options = new Option[] { new Option(Locale.get(options.asString())) };
					}
				}

				JsonValue next = value.get("next");

				if (next != null) {
					if (next.isArray()) {
						phrase.next = next.asStringArray();
					} else {
						phrase.next = new String[] { next.asString() };
					}
				}

				dt.phrases.add(phrase);
			}

			dialog.data.put(part.name, dt);
		}

		return dialog;
	}

	public DialogData get(String name) {
		return this.data.get(name);
	}

	public static class Phrase {
		public String name;
		public String string;
		public String owner;
		public Option[] options;
		public String[] next;
	}

	public static class Option {
		public float x;
		public float c = 1f;
		public String string;

		public Option(String string) {
			this.string = string;
		}
	}
}