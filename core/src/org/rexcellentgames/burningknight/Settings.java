package org.rexcellentgames.burningknight;

import com.badlogic.gdx.Gdx;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;

public class Settings {
	public static boolean fullscreen;
	public static boolean vsync = true;
	public static boolean blood = true;
	public static boolean gore = true;
	public static boolean uisfx = true;
	public static int quality = 1;
	public static float screenshake = 0.7f;
	public static float music = 0.5f;
	public static float sfx = 0.5f;
	public static String cursor = "cursor-standart";
	public static int cursorId = 0;

	public static final String[] cursors = new String[] {
		"cursor-standart",
		"cursor-small",
		"cursor-rect"
	};

	public static int getCursorId(String name) {
		for (int i = 0; i < cursors.length; i++) {
			if (cursors[i].equals(name)) {
				return i;
			}
		}

		return 0;
	}

	public static void load() {
		fullscreen = GlobalSave.isTrue("settings_fullscreen");
		blood = GlobalSave.isTrue("settings_blood");
		uisfx = GlobalSave.isTrue("settings_uisfx");
		gore = GlobalSave.isTrue("settings_gore");
		vsync = GlobalSave.isTrue("settings_vsync");
		quality = GlobalSave.getInt("settings_quality");
		screenshake = GlobalSave.getFloat("settings_screenshake");
		sfx = GlobalSave.getFloat("settings_sfx");
		music = GlobalSave.getFloat("settings_music");
		cursor = GlobalSave.getString("settings_cursor", "cursor-standart");

		cursorId = getCursorId(cursor);

		if (fullscreen) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		} else {
			Gdx.graphics.setWindowedMode(Display.GAME_WIDTH * 3, Display.GAME_HEIGHT * 3);
		}

		Gdx.graphics.setVSync(Settings.vsync);
	}

	public static void save() {
		GlobalSave.put("settings_fullscreen", fullscreen);
		GlobalSave.put("settings_blood", blood);
		GlobalSave.put("settings_uisfx", uisfx);
		GlobalSave.put("settings_gore", gore);
		GlobalSave.put("settings_vsync", vsync);

		GlobalSave.put("settings_quality", quality);
		GlobalSave.put("settings_screenshake", screenshake);
		GlobalSave.put("settings_sfx", sfx);
		GlobalSave.put("settings_music", music);

		GlobalSave.put("settings_cursor", cursor);
	}

	public static void generate() {
		GlobalSave.put("settings_fullscreen", false);
		GlobalSave.put("settings_blood", true);
		GlobalSave.put("settings_uisfx", true);
		GlobalSave.put("settings_gore", true);
		GlobalSave.put("settings_vsync", true);

		GlobalSave.put("settings_quality", 1);
		GlobalSave.put("settings_screenshake", 0.7f);
		GlobalSave.put("settings_sfx", 0.5f);
		GlobalSave.put("settings_music", 0.5f);

		GlobalSave.put("settings_cursor", "cursor-standart");
		cursorId = getCursorId(cursor);
	}
}