package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Noise;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.inventory.Inventory;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.Tween;

public class InventoryState extends State {
	private Inventory inventory;
	private UiInventory ui;
	public static int depth;
	public static Texture texture = new Texture(Gdx.files.internal("bricks.png"));

	@Override
	public void init() {
		super.init();

		Dungeon.buildDiscordBadge();
		Dungeon.dark = 0;

		Tween.to(new Tween.Task(1, 0.5f) {
			@Override
			public float getValue() {
				return 0;
			}

			@Override
			public void setValue(float value) {
				Dungeon.dark = value;
			}
		});

		Dungeon.white = 0;
		Dungeon.darkR = Dungeon.MAX_R;

		inventory = Player.instance.getInventory();

		if (Player.instance.ui == null) {
			depth = Dungeon.depth + 1;
			UiInventory inventory = new UiInventory(Player.instance.getInventory());
			Dungeon.ui.add(inventory);
		}

		ui = Player.instance.ui;
		ui.createSlots();

		Dungeon.area.destroy();

		Dungeon.ui.add(new UiButton("go", Display.UI_WIDTH / 2, (int) (ui.slots[0].y + 14 + 32)) {
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

	public static TextureRegion player = Graphics.getTexture("props-gobbo_full");

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

		Graphics.startAlphaShape();
		Graphics.shape.setProjectionMatrix(Camera.nil.combined);
		Color cl = ColorUtils.HSV_to_RGB(Dungeon.time * 20 % 360, 360, 360);
		Dungeon.setBackground2(new Color(cl.r * 0.4f, cl.g * 0.4f, cl.b * 0.4f, 1f));

		for (int i = 0; i < 65; i++) {
			float s = i * 0.015f;
			float mx = (Noise.instance.noise(Dungeon.time * 0.25f + s) * 128f);
			float my = (Noise.instance.noise( 3 + Dungeon.time * 0.25f + s) * 128f);
			float v = ((float) i) / 80f + 0.3f;

			Color color = ColorUtils.HSV_to_RGB((Dungeon.time * 20 - i * 1.4f) % 360, 360, 360);
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 0.5f);

			float a = (float) (Math.PI * i * 0.2f) + Dungeon.time * 2f;
			float w = i * 2 + 64;
			float d = i * 4f;
			float x = (float) (Math.cos(a) * d) + Display.GAME_WIDTH / 2 + mx * (((float) 56-i) / 56);
			float y = (float) (Math.sin(a) * d) + Display.GAME_HEIGHT / 2 + my * (((float) 56-i) / 56);

			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 1f, 1f, (float) Math.toDegrees(a + 0.1f));
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 0.9f);
			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 0.9f, 0.9f, (float) Math.toDegrees(a + 0.1f));
		}


		float i = 32;
		float mx = (Noise.instance.noise(Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56-i) / 56);
		float my = (Noise.instance.noise( 3 + Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56-i) / 56);

		Graphics.endAlphaShape();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Graphics.render(player, Display.GAME_WIDTH / 2 + mx, Display.GAME_HEIGHT / 2 + my, Dungeon.time * 650, 8, 8,false, false);
	}

	@Override
	public void renderUi() {
		ui.render();
		Dungeon.ui.render();
		Ui.ui.renderCursor();
	}
}