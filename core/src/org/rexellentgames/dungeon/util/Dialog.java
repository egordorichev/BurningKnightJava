package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexellentgames.dungeon.assets.Locale;

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
		public String string;
	}
}