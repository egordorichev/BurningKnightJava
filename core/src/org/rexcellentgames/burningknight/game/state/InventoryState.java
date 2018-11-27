package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;

public class InventoryState extends State {
	// fixme: game should keep track if it saved in this state or in-game

	private Inventory inventory;
	private UiInventory ui;
	private UiButton go;
	public static int depth;

	@Override
	public void init() {
		super.init();

		Dungeon.setBackground2(Color.valueOf("#555555"));

		inventory = Player.instance.getInventory();
		ui = Player.instance.ui;
		ui.createSlots();

		go = (UiButton) Dungeon.ui.add(new UiButton("go", Display.UI_WIDTH / 2, (int) (ui.slots[0].y + 20 + 32)) {
			@Override
			public void onClick() {
				super.onClick();
				Dungeon.goToLevel(depth);
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

		TextureRegion texture = Graphics.getTexture("item-missing");

		float tx = texture.getU();
		float ty = texture.getV();
		float tw = texture.getU2();
		float th = texture.getV2();

		{
			float h = Display.UI_HEIGHT;
			float x = 0;
			float y = 0;
			float c = Graphics.batch.getColor().toFloatBits();

			// fixme: figure out uv so that it gets transformed
			float[] vert = new float[] {
				x, y, c, tx, ty + th,
				x + fm + mvx, y + xm + mvy, c, tx + tw, ty,
				x + fm + mvx, y + xm + mvy, c, tx + tw, ty,
				x, y + h, c, tx, ty + th
			};

			// Graphics.batch.draw(texture.getTexture(), vert, 0, 20);
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
		ui.render();
		Dungeon.ui.render();
		Ui.ui.renderCursor();
	}
}