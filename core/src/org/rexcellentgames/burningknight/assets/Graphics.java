package org.rexcellentgames.burningknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.mod.Mod;
import org.rexcellentgames.burningknight.util.Log;

import java.util.HashMap;

public class Graphics {
	public static SpriteBatch batch;
	public static ShapeRenderer shape;
	public static GlyphLayout layout;
	public static TextureAtlas atlas;

	public static BitmapFont small;
	public static BitmapFont medium;
	public static BitmapFont smallSimple;
	public static BitmapFont mediumSimple;

	public static FrameBuffer shadows;
	public static FrameBuffer surface;
	public static FrameBuffer text;
	public static FrameBuffer map;
	public static FrameBuffer blood;

	private static HashMap<String, HashMap<String, TextureRegion>> modSprites = new HashMap<>();

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

	public static void startShadows() {
		Graphics.batch.end();

		Graphics.surface.end(Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
			Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());

		Graphics.shadows.begin();

		Graphics.batch.setProjectionMatrix(Camera.game.combined);
		Graphics.batch.begin();
	}

	public static void endShadows() {
		Graphics.batch.end();

		Graphics.shadows.end(Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
			Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());

		Graphics.surface.begin();
		Graphics.batch.begin();
	}

	public static void targetAssets() {
		Log.info("Init assets...");
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		layout = new GlyphLayout();

		shadows = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		surface = new FrameBuffer(Pixmap.Format.RGBA8888, Display.GAME_WIDTH, Display.GAME_HEIGHT, false);
		text = new FrameBuffer(Pixmap.Format.RGBA8888, Display.GAME_WIDTH, Display.GAME_HEIGHT, false);
		map = new FrameBuffer(Pixmap.Format.RGBA8888, Display.UI_WIDTH, Display.UI_HEIGHT, false);
		blood = new FrameBuffer(Pixmap.Format.RGBA8888, Display.GAME_WIDTH, Display.GAME_HEIGHT, false);

		Assets.manager.load("atlas/atlas.atlas", TextureAtlas.class);

		FileHandleResolver resolver = new InternalFileHandleResolver();

		Assets.manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		Assets.manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		small = new BitmapFont(Gdx.files.internal("fonts/small.fnt"), Gdx.files.internal("fonts/small.png"), false);
		medium = new BitmapFont(Gdx.files.internal("fonts/large.fnt"), Gdx.files.internal("fonts/large.png"), false);

		smallSimple = new BitmapFont(Gdx.files.internal("fonts/small_simple.fnt"), Gdx.files.internal("fonts/small_simple.png"), false);
		mediumSimple = new BitmapFont(Gdx.files.internal("fonts/large_simple.fnt"), Gdx.files.internal("fonts/large_simple.png"), false);
	}

	public static void loadAssets() {
		atlas = Assets.manager.get("atlas/atlas.atlas");

		small.getData().markupEnabled = true;
		small.getData().setLineHeight(10);

		medium.getData().markupEnabled = true;

		smallSimple.getData().markupEnabled = true;
		smallSimple.getData().setLineHeight(10);

		mediumSimple.getData().markupEnabled = true;

		new Ui();
	}
	
	public static void loadModAssets(Mod mod) {
	  HashMap<String, TextureRegion> regions = new HashMap<>();
	  
	  for (FileHandle file : mod.getSpritesDirectory().list()) {
	    if (!file.isDirectory() && file.extension().equals("png")) {
	      regions.put(file.nameWithoutExtension(), new TextureRegion(new Texture(file)));
      }
    }

    modSprites.put(mod.getId(), regions);
  }

	public static void resize(int w, int h) {
		w = Math.max(w, Display.GAME_WIDTH);
		h = Math.max(h, Display.GAME_HEIGHT);

		shadows.dispose();
		shadows = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
	}

	public static TextureRegion getTexture(String name) {
		TextureRegion region = atlas.findRegion(name);

		if (region == null) {
			Log.error("Texture '" + name + "' is not found!");
			
			return Item.missing;
		}

		return region;
	}
	
	public static TextureRegion getModTexture(String modId, String name) {
		return modSprites.get(modId).get(name);
  }

	public static void write(String s, BitmapFont font, float x, float y) {
		write(s, font, x, y, 0, 1, 1);
	}

	public static void write(String s, BitmapFont font, float x, float y, float a, float sx, float sy) {
		Graphics.batch.end();
		Graphics.surface.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.layout.setText(font, s);

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		font.draw(Graphics.batch, s, 2, 16);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.surface.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.game.combined);

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.draw(texture, x + 2, y, Graphics.layout.width / 2 + 4, Graphics.layout.height,
			Graphics.layout.width, Graphics.layout.height * 2, sx, sy, a,
			0, 0, (int) Graphics.layout.width + 4, (int) Graphics.layout.height * 2, false, true);
	}

	public static void print(String s, BitmapFont font, float x, float y) {
		font.draw(batch, s, x, y + (font == medium || font == mediumSimple ? 16 : 8));
	}

	public static void printCenter(String s, BitmapFont font, float x, float y) {
		layout.setText(font, s);

		print(s, font, (Display.UI_WIDTH - layout.width) / 2 + x, y);
	}

	public static void print(String s, BitmapFont font, float y) {
		layout.setText(font, s);

		print(s, font, (Display.UI_WIDTH - layout.width) / 2, y);
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
		w -= z;
		h -= z;
		x += z / 2;
		y -= z / 2;

		Graphics.shape.ellipse(x - 1, y - h / 4, w + 2, h / 2);
	}

	public static void shadowSized(float x, float y, float w, float h, float s) {
		w -= s;
		h -= s;
		x += s / 2;

		Graphics.shape.ellipse(x - 1, y - h / 4, w + 2, h / 2);
	}

	public static void shadow(float x, float y, float w, float h, float z, float a) {
		w -= z;
		h -= z;
		x += z / 2;
		y -= z / 2;

		Graphics.shape.setColor(1, 1, 1, a);
		Graphics.shape.ellipse(x - 1, y - h / 4, w + 2, h / 2);
		Graphics.shape.setColor(1, 1, 1, 1);
	}

	public static void startShape() {
		Graphics.batch.end();
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
	}

	public static void endShape() {
		Graphics.shape.end();
		Graphics.batch.begin();
	}

	public static void startAlphaShape() {
		Graphics.batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
	}

	public static void endAlphaShape() {
		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();
	}

	public static void destroy() {
		if (atlas == null) {
			return;
		}

		map.dispose();
		atlas.dispose();
		batch.dispose();
		shape.dispose();
		shadows.dispose();
		surface.dispose();
		blood.dispose();
	}
}