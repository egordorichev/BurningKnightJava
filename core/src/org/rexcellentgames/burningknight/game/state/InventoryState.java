package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.inventory.Inventory;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.ui.UiButton;

public class InventoryState extends State {
	private Inventory inventory;
	private UiInventory ui;
	private UiButton go;
	public static int depth;
	public static Texture texture = new Texture(Gdx.files.internal("bricks.png"));

	@Override
	public void init() {
		super.init();

		texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		Dungeon.dark = 1;
		Dungeon.white = 0;
		Dungeon.darkR = Dungeon.MAX_R;

		Dungeon.setBackground2(Color.valueOf("#555555"));

		inventory = Player.instance.getInventory();

		if (Player.instance.ui == null) {
			depth = Dungeon.depth + 1;
			UiInventory inventory = new UiInventory(Player.instance.getInventory());
			Dungeon.ui.add(inventory);
		}

		ui = Player.instance.ui;
		ui.createSlots();

		Dungeon.area.destroy();

		go = (UiButton) Dungeon.ui.add(new UiButton("go", Display.UI_WIDTH / 2, (int) (ui.slots[0].y + 14 + 32)) {
			@Override
			public void onClick() {
				super.onClick();

				for (int i = 8; i < inventory.getSize(); i++) {
					inventory.setSlot(i, null);
				}

				GameSave.inventory = false;
				SaveManager.saveGame();
				SaveManager.saveGames();
				Dungeon.goToLevel(InventoryState.depth);
			}
		});
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		ui.update(dt);
	}

	public static ShaderProgram shader;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert").readString(), Gdx.files.internal("shaders/tunnel.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	@Override
	public void render() {
		super.render();
		texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.end();
		shader.begin();
		shader.setUniformf("sign", 1);
		shader.setUniformf("time", Dungeon.time * 0.1f);
		shader.end();
		Graphics.batch.setShader(shader);
		Graphics.batch.begin();

		float mvx = 0;//(float) (Display.UI_HEIGHT * 0.25f * Math.cos(Dungeon.time));
		float mvy = 0;//(float) (Display.UI_WIDTH * 0.25f * Math.sin(Dungeon.time));
		float xm = (float) (Display.UI_HEIGHT * 0.5f);
		float fm = (float) (Display.UI_WIDTH * 0.5f);

		TextureRegion region = new TextureRegion(texture);

		float u = region.getU();
		float v = region.getV();
		float u2 = region.getU2();
		float v2 = region.getV2();

		{
			float h = Display.UI_HEIGHT;
			float x = 0;
			float y = 0;
			float c = Graphics.batch.getColor().toFloatBits();

			float[] vert = new float[] {
				x, y,             c, u, v,
				x, y + h,         c, u, v2,
				x + fm, y + xm,   c, u2, v + (v2 - v) * 0.5f,
				x, y,             c, u, v
			};

			Graphics.batch.draw(texture, vert, 0, 20);
		}

		{
			float h = Display.UI_HEIGHT;
			float w = Display.UI_WIDTH;
			float x = 0;
			float y = 0;
			float c = Graphics.batch.getColor().toFloatBits();

			float[] vert = new float[] {
				x + w, y,             c, u, v,
				x + w, y + h,         c, u, v2,
				x + fm, y + xm,   c, u2, v + (v2 - v) * 0.5f,
				x + w, y,             c, u, v
			};

			Graphics.batch.draw(texture, vert, 0, 20);
		}

		{
			float h = Display.UI_HEIGHT;
			float w = Display.UI_WIDTH;
			float x = 0;
			float y = 0;
			float c = Graphics.batch.getColor().toFloatBits();

			float[] vert = new float[] {
				x, y + h,             c, u, v,
				x + w, y + h,         c, u, v2,
				x + w * 0.5f, y + h * 0.5f,   c, u2, v + (v2 - v) * 0.5f,
				x, y + h,             c, u, v
			};

			Graphics.batch.draw(texture, vert, 0, 20);
		}

		{
			float h = Display.UI_HEIGHT;
			float w = Display.UI_WIDTH;
			float x = 0;
			float y = 0;
			float c = Graphics.batch.getColor().toFloatBits();

			float[] vert = new float[] {
				x, y,             c, u, v,
				x + w, y,         c, u, v2,
				x + w * 0.5f, y + h * 0.5f,   c, u2, v + (v2 - v) * 0.5f,
				x, y,             c, u, v
			};

			Graphics.batch.draw(texture, vert, 0, 20);
		}

		/*
		Graphics.batch.end();
		shader.begin();
		shader.setUniformf("sign", 1);
		shader.setUniformf("time", Dungeon.time);
		shader.end();
		Graphics.batch.begin();

		{
			float h = Display.UI_HEIGHT;
			float x = Display.UI_WIDTH;
			float y = 0;
			float c = Graphics.batch.getColor().toFloatBits();
			TextureRegion texture = InGameState.noise;

			float tx = texture.getU();
			float ty = texture.getV();
			float tw = texture.getU2();
			float th = texture.getV2();

			float[] vert = new float[]{
				x, y, c, tx, ty + th,
				x - fm + mvx, y + xm + mvy, c, tx + tw, ty + th,
				x - fm + mvx, y + xm + mvy, c, tx + tw, ty,
				x, y + h, c, tx, ty
			};

			Graphics.batch.draw(texture.getTexture(), vert, 0, 20);
		}

		Graphics.batch.end();
		shader.begin();
		shader.setUniformf("sign", -1);
		shader.setUniformf("time", Dungeon.time);
		shader.end();
		Graphics.batch.begin();

		{
			float w = Display.UI_WIDTH;
			float x = 0;
			float y = Display.UI_HEIGHT;
			float c = Graphics.batch.getColor().toFloatBits();
			TextureRegion texture = InGameState.noise;

			float tx = texture.getU();
			float ty = texture.getV();
			float tw = texture.getU2();
			float th = texture.getV2();

			float[] vert = new float[]{
				x, y, c, tx, ty + th,
				x + fm + mvx, y - xm + mvy, c, tx + tw, ty + th,
				x + fm + mvx, y - xm + mvy, c, tx + tw, ty,
				x + w, y, c, tx, ty
			};

			Graphics.batch.draw(texture.getTexture(), vert, 0, 20);
		}

		Graphics.batch.end();
		shader.begin();
		shader.setUniformf("sign", -1);
		shader.setUniformf("time", Dungeon.time);
		shader.end();
		Graphics.batch.begin();

		{
			float w = Display.UI_WIDTH;
			float x = 0;
			float y = 0;
			float c = Graphics.batch.getColor().toFloatBits();
			TextureRegion texture = InGameState.noise;

			float tx = texture.getU();
			float ty = texture.getV();
			float tw = texture.getU2();
			float th = texture.getV2();

			float[] vert = new float[]{
				x, y, c, tx, ty + th,
				x + fm + mvx, y + xm + mvy, c, tx + tw, ty + th,
				x + fm + mvx, y + xm + mvy, c, tx + tw, ty,
				x + w, y, c, tx, ty
			};

			Graphics.batch.draw(texture.getTexture(), vert, 0, 20);
		}*/

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public void renderUi() {
		//ui.render();
		//Dungeon.ui.render();
		//Ui.ui.renderCursor();
	}
}