package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Graphics {
	public static SpriteBatch batch = new SpriteBatch();
	public static ShapeRenderer shape = new ShapeRenderer();
	public static GlyphLayout layout = new GlyphLayout();
	public static Texture tiles;
	public static Texture sprites;
	public static Texture items;
	public static Texture buffs;
	public static Texture ui;
	public static Texture effects;
	public static BitmapFont small;
	public static BitmapFont medium;

	public static void init() {
		tiles = new Texture("sprites/tiles-1.png");
		sprites = new Texture("sprites/sprites.png");
		items = new Texture("sprites/items.png");
		buffs = new Texture("sprites/buffs.png");
		ui = new Texture("sprites/ui.png");
		effects = new Texture("sprites/effects.png");

		tiles.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		sprites.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		items.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		buffs.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		ui.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		effects.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		small = generateFont("fonts/pico.ttf", 4);
		medium = generateFont("fonts/large.ttf", 16);
	}

	private static BitmapFont generateFont(String path, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = size;
		parameter.borderWidth = 1;
		parameter.borderColor = Color.BLACK;
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;

		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		return font;
	}

	public static void render(Texture texture, int tile, float x, float y) {
		render(texture, tile, x, y, 1, 1, 0, 8, 8, false, false);
	}

	public static void render(Texture texture, int tile, float x, float y, int w, int h) {
		render(texture, tile, x, y, w, h, 0, w * 8, h * 8, false, false);
	}

	public static void render(Texture texture, int tile, float x, float y, int w, int h, float a, float ox, float oy,
		boolean fx, boolean fy) {

		int xx = tile % 32 * 16;
		int yy = (int) (Math.floor(tile / 32) * 16);

		Graphics.batch.draw(texture, x, y, ox, oy, 16 * w, 16 * h, 1, 1, a,
			xx, yy, 16 * w, 16 * h, fx, fy);
	}

	public static void render(Texture texture, int tile, float x, float y, int w, int h, float a, float ox, float oy,
			boolean fx, boolean fy, float dw, float dh) {

		int xx = tile % 32 * 16;
		int yy = (int) (Math.floor(tile / 32) * 16);

		Graphics.batch.draw(texture, x, y, ox, oy, 16 * w, 16 * h, 1, 1, a,
			xx, yy, 16 * w, 16 * h, fx, fy);
	}

	public static void destroy() {
		tiles.dispose();
		sprites.dispose();
	}
}