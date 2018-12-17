package org.rexcellentgames.burningknight;

import com.badlogic.gdx.Gdx;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;

public class Settings {
	public static boolean fullscreen;
	public static boolean vsync = true;
	public static boolean blood = true;
	public static boolean gore = true;
	public static boolean uisfx = true;
	public static boolean borderless = false;
	public static boolean speedrun_mode = true;
	public static boolean speedrun_timer = true;
	public static int quality = 2;
	public static float screenshake = 0.7f;
	public static float music = 0.5f;
	public static float sfx = 0.75f;
	public static String cursor = "cursor-standart";
	public static boolean rotateCursor = true;
	public static int cursorId = 0;
	public static int side_art = 0;
	public static float freeze_frames = 0.5f;
	public static float flash_frames = 0.5f;
	public static boolean vegan = false;

	public static final String[] cursors = new String[] {
		"cursor-standart",
		"cursor-small",
		"cursor-rect",
		"cursor-corner",
		"cursor-sniper",
		"cursor-round-sniper",
		"cursor-cross",
		"cursor-nt",
		"native"
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
		speedrun_mode = GlobalSave.isTrue("settings_sm");
		speedrun_timer = GlobalSave.isTrue("settings_st");
		borderless = GlobalSave.isTrue("settings_bl");
		side_art = GlobalSave.getInt("settings_sa");
		quality = GlobalSave.getInt("settings_quality");
		screenshake = GlobalSave.getFloat("settings_screenshake");
		sfx = GlobalSave.getFloat("settings_sfx");
		Dungeon.colorBlind = GlobalSave.getFloat("settings_cb");
		freeze_frames = GlobalSave.getFloat("settings_frf");
		flash_frames = GlobalSave.getFloat("settings_ff");
		music = GlobalSave.getFloat("settings_music");
		cursor = GlobalSave.getString("settings_cursor", "cursor-standart");
		rotateCursor = GlobalSave.isTrue("settings_rotate_cursor", true);
		vegan = GlobalSave.isTrue("settings_v", false);
		Dungeon.fpsY = GlobalSave.isTrue("settings_sf", false) ? 18 : 0;

		cursorId = getCursorId(cursor);

		if (fullscreen) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		} else {
			Gdx.graphics.setWindowedMode(Display.GAME_WIDTH * 3, Display.GAME_HEIGHT * 3);
		}

		if (borderless) {
			Gdx.graphics.setUndecorated(Settings.borderless);
		}

		Gdx.graphics.setVSync(Settings.vsync);
		Dungeon.tweenTimer(Settings.speedrun_timer);
	}

	public static void save() {
		GlobalSave.put("settings_fullscreen", fullscreen);
		GlobalSave.put("settings_blood", blood);
		GlobalSave.put("settings_uisfx", uisfx);
		GlobalSave.put("settings_gore", gore);
		GlobalSave.put("settings_vsync", vsync);
		GlobalSave.put("settings_sm", speedrun_mode);
		GlobalSave.put("settings_st", speedrun_timer);
		GlobalSave.put("settings_bl", borderless);
		GlobalSave.put("settings_sa", side_art);
		GlobalSave.put("settings_frf", freeze_frames);
		GlobalSave.put("settings_ff", flash_frames);

		GlobalSave.put("settings_quality", quality);
		GlobalSave.put("settings_screenshake", screenshake);
		GlobalSave.put("settings_sfx", sfx);
		GlobalSave.put("settings_music", music);

		GlobalSave.put("settings_cursor", cursor);
		GlobalSave.put("settings_rotate_cursor", rotateCursor);

		GlobalSave.put("settings_cb", Dungeon.colorBlind);
		GlobalSave.put("settings_v", vegan);
		GlobalSave.put("settings_sf", Dungeon.fpsY == 18);
	}

	public static void generate() {
		GlobalSave.put("settings_fullscreen", false);
		GlobalSave.put("settings_blood", true);
		GlobalSave.put("settings_uisfx", true);
		GlobalSave.put("settings_gore", true);
		GlobalSave.put("settings_vsync", true);
		GlobalSave.put("settings_sm", false);
		GlobalSave.put("settings_st", false);
		GlobalSave.put("settings_bl", false);
		GlobalSave.put("settings_sa", 0);
		GlobalSave.put("settings_frf", 0.5f);
		GlobalSave.put("settings_ff", 0.5f);

		GlobalSave.put("settings_quality", 2);
		GlobalSave.put("settings_screenshake", 0.3f);
		GlobalSave.put("settings_music", 0.5f);
		GlobalSave.put("settings_sfx", 0.75f);

		GlobalSave.put("settings_cursor", "cursor-standart");
		GlobalSave.put("settings_rotate_cursor", true);

		GlobalSave.put("settings_cb", 0);
		GlobalSave.put("settings_v", false);

		cursorId = getCursorId(cursor);
		Dungeon.tweenTimer(false);
	}
}