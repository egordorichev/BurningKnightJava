package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.util.Log;

import java.io.File;
import java.io.FilenameFilter;

public class Graphics {
	public static SpriteBatch batch;
	public static ShapeRenderer shape;
	public static GlyphLayout layout;
	public static TextureAtlas atlas;
	public static BitmapFont small;
	public static BitmapFont medium;
	public static AssetManager manager;

	public static void init() {
		Log.info("Init assets...");

		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		layout = new GlyphLayout();

		manager = new AssetManager();
		manager.load("atlas/atlas.atlas", TextureAtlas.class);

		FileHandleResolver resolver = new InternalFileHandleResolver();

		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		manager.load("sfx/Bomb_exploding.wav", Sound.class);
		manager.load("sfx/Bomb_placing.wav", Sound.class);
		manager.load("sfx/Potion.wav", Sound.class);
		manager.load("sfx/Scroll.wav", Sound.class);
		manager.load("sfx/Woosh.wav", Sound.class);
		manager.load("sfx/BK_sfx.wav", Sound.class);

		for (int i = 1; i < 6; i++) {
			manager.load("sfx/step_gobbo_normal_" + i + ".wav", Sound.class);
			manager.load("sfx/step_gobbo_water_" + i + ".wav", Sound.class);
		}

		generateFont("fonts/small.ttf", 16);
		generateFont("fonts/large.ttf", 16);

		manager.finishLoading();

		small = manager.get("fonts/small.ttf");
		medium = manager.get("fonts/large.ttf");
		atlas = manager.get("atlas/atlas.atlas");

		small.getData().markupEnabled = true;
		medium.getData().markupEnabled = true;
	}

	public static Sound getSound(String sfx) {
		return manager.get(sfx, Sound.class);
	}

	public static TextureRegion getTexture(String name) {
		TextureRegion region = atlas.findRegion(name);

		if (region == null) {
			Log.error("Texture '" + name + "' is not found!");
		}

		return region;
	}

	private static void generateFont(String path, int size) {
		FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();

		font.fontFileName = path;
		font.fontParameters.size = size;
		font.fontParameters.borderColor = Color.BLACK;
		font.fontParameters.borderWidth = 1;

		manager.load(path, BitmapFont.class, font);
	}

	public static void print(String s, BitmapFont font, float x, float y) {
		font.draw(batch, s, x, y + (font == medium ? 16 : 8));
	}

	public static void print(String s, BitmapFont font, float y) {
		layout.setText(font, s);

		print(s, font, (Display.GAME_WIDTH - layout.width) / 2, y);
	}

	public static void render(TextureRegion texture, float x, float y) {
		float ox = texture.getRegionWidth() / 2;
		float oy = texture.getRegionHeight() / 2;

		render(texture, x + ox, y + oy, 0, ox, oy, false, false);
	}

	public static void render(TextureRegion texture, float x, float y, float a, float ox, float oy,
		boolean fx, boolean fy) {

		Graphics.batch.draw(texture, x - ox + (fx ? texture.getRegionWidth() : 0),
			y - oy + (fy ? texture.getRegionHeight() : 0),
			ox, oy, texture.getRegionWidth(), texture.getRegionHeight(), fx ? -1 : 1, fy ? -1 : 1, a);
	}

	public static void render(TextureRegion texture, float x, float y, float a, float ox, float oy,
	                          boolean fx, boolean fy, float sx, float sy) {

		Graphics.batch.draw(texture, x - ox + (fx ? texture.getRegionWidth() : 0),
			y - oy + (fy ? texture.getRegionHeight() : 0),
			ox, oy, texture.getRegionWidth(), texture.getRegionHeight(), sx, sy, a);
	}

	public static void destroy() {
		atlas.dispose();
		manager.dispose();
		batch.dispose();
		shape.dispose();
	}
}