package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.util.Log;

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

		generateFont("fonts/pico.ttf", 4);
		generateFont("fonts/large.ttf", 16);

		manager.finishLoading();

		small = manager.get("fonts/pico.ttf");
		medium = manager.get("fonts/large.ttf");
		atlas = manager.get("atlas/atlas.atlas");

		small.getData().markupEnabled = true;
		medium.getData().markupEnabled = true;
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

	public static void render(TextureRegion texture, float x, float y) {
		render(texture, x, y, 0, texture.getRegionWidth() / 2,
			texture.getRegionHeight() / 2, false, false);
	}

	public static void render(TextureRegion texture, float x, float y, float a, float ox, float oy,
		boolean fx, boolean fy) {

		Graphics.batch.draw(texture, x, y,
			ox, oy, texture.getRegionWidth(), texture.getRegionHeight(), fx ? -1 : 1, fy ? -1 : 1, a);
	}

	public static void destroy() {
		atlas.dispose();
		manager.dispose();
		batch.dispose();
		shape.dispose();
	}
}