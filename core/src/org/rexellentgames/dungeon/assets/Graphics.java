package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.util.Log;

import java.util.HashMap;

public class Graphics {
	public static SpriteBatch batch;
	public static ShapeRenderer shape;
	public static GlyphLayout layout;
	public static TextureAtlas atlas;
	public static BitmapFont small;
	public static BitmapFont medium;
	public static AssetManager manager;
	public static FrameBuffer shadows;
	public static FrameBuffer text;
	public static HashMap<String, Float> volumes = new HashMap<>();

	public static void delay() {
		delay(20);
	}

	public static void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static Color color;

	public static void startShadows() {
		color = Graphics.batch.getColor();
		Graphics.batch.end();
		Graphics.shadows.begin();
		Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);

		Graphics.batch.begin();
		Graphics.batch.setColor(1, 1, 1, color.a);
	}

	public static void endShadows() {
		Graphics.batch.setColor(color);
		Graphics.batch.end();

		Graphics.shadows.end(Camera.instance.viewport.getScreenX(), Camera.instance.viewport.getScreenY(),
			Camera.instance.viewport.getScreenWidth(), Camera.instance.viewport.getScreenHeight());
		Camera.instance.viewport.apply();
		Graphics.batch.begin();
	}

	public static void init() {
		Log.info("Init assets...");
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		layout = new GlyphLayout();
		shadows = new FrameBuffer(Pixmap.Format.RGBA8888, Camera.instance.viewport.getScreenWidth(), Camera.instance.viewport.getScreenHeight(), false);
		text = new FrameBuffer(Pixmap.Format.RGBA8888, Display.GAME_WIDTH, Display.GAME_HEIGHT, false);

		manager = new AssetManager();
		manager.load("atlas/atlas.atlas", TextureAtlas.class);

		FileHandleResolver resolver = new InternalFileHandleResolver();

		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));


		JsonReader reader = new JsonReader();
		JsonValue root = reader.parse(Gdx.files.internal("sfx/sfx.json"));

		for (JsonValue name : root) {
			manager.load("sfx/" + name.toString() + ".mp3", Sound.class);
			volumes.put(name.toString(), 1f);
		}

		root = reader.parse(Gdx.files.internal("music/music.json"));

		for (JsonValue name : root) {
			manager.load("music/" + name.toString() + ".mp3", Music.class);
		}

		generateFont("fonts/small.ttf", 16);
		generateFont("fonts/large.ttf", 16);

		manager.finishLoadingAsset("fonts/large.ttf");

		medium = manager.get("fonts/large.ttf");
		medium.getData().markupEnabled = true;

		FileHandle file = Gdx.files.external("sfx.json");

		if (file.exists()) {
			root = reader.parse(file);

			for (JsonValue name : root) {
				Log.info("Set " + name.name + " to " + name.asFloat());
				volumes.put(name.name, name.asFloat());
			}
		}
	}

	public static boolean updateLoading() {
		boolean val = manager.update();

		if (val && small == null) {
			small = manager.get("fonts/small.ttf");
			atlas = manager.get("atlas/atlas.atlas");

			small.getData().markupEnabled = true;
			small.getData().setLineHeight(10);

			new Ui();
		}

		return val;
	}

	public static float getPercent() {
		return manager.getProgress();
	}

	public static long playSfx(String name) {
		return playSfx(name, 1f, 1f);
	}

	public static long playSfx(String name, float volume) {
		return playSfx(name, volume, 1f);
	}

	public static long playSfx(String name, float volume, float pitch) {
		if (name.startsWith("menu") && !Settings.uisfx) {
			return 0;
		}

		Sound sound = getSound(name);

		long id = sound.play(volume * volumes.get(name) * Settings.sfx);
		sound.setPitch(id, pitch);

		return id;
	}

	public static void resize(int w, int h) {
		shadows.dispose();

		float z = Math.max(1, Camera.instance.getCamera().zoom);

		shadows = new FrameBuffer(Pixmap.Format.RGBA8888, (int) Math.max(1, Math.ceil(Camera.instance.viewport.getScreenWidth() * z)),
			(int) Math.max(1, Math.ceil(Camera.instance.viewport.getScreenHeight() * z)), false);
	}

	public static Sound getSound(String sfx) {
		Sound sound = manager.get("sfx/" + sfx + ".mp3", Sound.class);

		if (sound == null) {
			Log.error("Sfx '" + sfx + "' is not found!");
		}

		return sound;
	}

	public static Music getMusic(String name) {
		Music music = manager.get("music/" + name + ".mp3", Music.class);

		if (music == null) {
			Log.error("Music '" + name + "' is not found!");
		}

		return music;
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
		font.fontParameters.characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,;:?!-_~#\"'&()[]|`/\\@°+=*%€$£¢<>©®ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØŒÙÚÛÜÝÞàáâãäåæçèéêëìíîïðñòóôõöøœùúûüýþßÿ¿¡АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";

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

	public static void shadow(float x, float y, float w, float h) {
		shadow(x, y, w, h, 0);
	}

	public static void shadow(float x, float y, float w, float h, float z) {
		startShadows();
		Graphics.batch.end();
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		w -= z;
		h -= z;
		x += z / 2;
		y -= z / 2;

		Graphics.shape.ellipse(x - 1, y - h / 4, w + 2, h / 2);

		Graphics.shape.end();
		Graphics.batch.begin();
		endShadows();
	}

	public static void destroy() {
		atlas.dispose();
		manager.dispose();
		batch.dispose();
		shape.dispose();
		shadows.dispose();
	}
}