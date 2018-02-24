package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Graphics {
	public static SpriteBatch batch = new SpriteBatch();
	public static Texture tiles;
	public static Texture sprites;

	public static void init() {
		tiles = new Texture("sprites/tiles-1.png");
		sprites = new Texture("sprites/sprites.png");

		tiles.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		sprites.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
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