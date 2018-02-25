package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Graphics {
	public static SpriteBatch batch = new SpriteBatch();
	public static Texture tiles;
	public static Texture sprites;
	public static Texture items;
	public static Texture buffs;
	public static Texture ui;
	public static BitmapFont small;
	public static BitmapFont medium;

	public static void init() {
		tiles = new Texture("sprites/tiles-1.png");
		sprites = new Texture("sprites/sprites.png");
		items = new Texture("sprites/items.png");
		buffs = new Texture("sprites/buffs.png");
		ui = new Texture("sprites/ui.png");

		tiles.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		sprites.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		items.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		buffs.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		ui.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		small = generateFont("fonts/pico.ttf", 4);
		medium = generateFont("fonts/font.ttf", 12);
	}

	private static BitmapFont generateFont(String path, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = size;
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;

		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		return font;
	}

	public static void render(Texture texture, int tile, float x, float y) {
		render(texture, tile, x, y, 1, 1);
	}

	public static void render(Texture texture, int tile, float x, float y, int w, int h) {
		int xx = tile % 32 * 16;
		int yy = (int) (Math.floor(tile / 32) * 16);

		Graphics.batch.draw(texture, x, y, 16 * w, 16 * h,
			xx, yy, 16 * w, 16 * h, false, false);
	}

	public static void destroy() {
		tiles.dispose();
		sprites.dispose();
	}
}