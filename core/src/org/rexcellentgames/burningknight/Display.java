package org.rexcellentgames.burningknight;

public class Display {
	public static final int GAME_WIDTH = 640 / 2;
	public static final int GAME_HEIGHT = 360 / 2;
	public static final float UI_SCALE = 1.5f;
	public static int UI_WIDTH = (int) (GAME_WIDTH * UI_SCALE);
	public static int UI_HEIGHT = (int) (GAME_HEIGHT * UI_SCALE);

	public static final int UI_WIDTH_MAX = (int) (GAME_WIDTH * 1.5f);
	public static final int UI_HEIGHT_MAX = (int) (GAME_HEIGHT * 1.5f);
	public static final int UI_WIDTH_MIN = (int) (GAME_WIDTH * 1.5f);
	public static final int UI_HEIGHT_MIN = (int) (GAME_HEIGHT * 1.5f);
}