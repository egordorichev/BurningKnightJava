package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Graphics {
	public static SpriteBatch batch = new SpriteBatch();
	public static Texture spriteSheet;

	public static void init() {
		spriteSheet = new Texture("sprites/sprites.png");
	}

	public static void destroy() {
		spriteSheet.dispose();
	}

	public static TextureRegion getTexture(int x, int y) {
		return new TextureRegion(spriteSheet, x, y, 16, 16);
	}
}