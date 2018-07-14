package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Tween;

public class UiMap extends UiEntity {
	{
		depth = 16;
		isSelectable = false;
	}

	public static boolean large;
	private float xc;
	private float yc;
	private static TextureRegion frame = Graphics.getTexture("ui-minimap");
	private float my;
	private UiButton plus;
	private UiButton minus;
	private UiButton hide;
	private UiButton show;

	protected void hide() {
		Tween.to(new Tween.Task(96, 0.4f) {
			@Override
			public float getValue() {
				return my;
			}

			@Override
			public void setValue(float value) {
				my = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();

				did = false;
			}
		});

		did = true;
		GlobalSave.put("hide_minimap", true);
	}

	protected void show() {
		Tween.to(new Tween.Task(0, 0.4f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return my;
			}

			@Override
			public void setValue(float value) {
				my = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();

				did = false;
			}
		});

		did = true;
		GlobalSave.put("hide_minimap", false);
	}

	private boolean did;

	@Override
	public void init() {
		super.init();
		setSize();

		if (GlobalSave.isTrue("hide_minimap")) {
			this.my = 96;
		}

		final UiMap self = this;

		hide = new UiImageButton("ui-hide_button", (int) x + 4, (int) y - 3) {
			@Override
			public void onClick() {
				super.onClick();
				hide();
			}

			@Override
			public void render() {
				if (!large) {
					this.y = self.y + my - 3;
					super.render();
				}
			}
		};

		Dungeon.ui.add(hide);

		show = new UiImageButton("ui-show", Display.GAME_WIDTH - 4 - 9, (int) y - 3) {
			@Override
			public void onClick() {
				super.onClick();
				show();
			}

			private float vl;

			@Override
			public void render() {
				if (!large) {
					float dx = Input.instance.uiMouse.x - this.x - 4;
					float dy = Input.instance.uiMouse.y - this.y - 4;
					float d = (float) Math.sqrt(dx * dx + dy * dy);
					float dt = Gdx.graphics.getDeltaTime() * 8;

					if (d > 100f) {
						vl += (1 - vl) * dt;
					} else {
						vl += (-vl) * dt;
					}

					this.y = 96 - my + Display.GAME_HEIGHT - MathUtils.clamp(0, 8, 8 - vl * 8);
					super.render();
				}
			}
		};

		Dungeon.ui.add(show);

		this.plus = new UiImageButton("ui-plus", (int) x + 41, (int) y - 3) {
			@Override
			public void onClick() {
				super.onClick();

				plus();
			}

			@Override
			public void render() {
				if (!large) {
					this.y = self.y + my - 3;
					super.render();
				}
			}
		};

		Dungeon.ui.add(plus);

		this.minus = new UiImageButton("ui-minus", (int) x + 51, (int) y - 3) {
			@Override
			public void onClick() {
				super.onClick();

				minus();
			}

			@Override
			public void render() {
				if (!large) {
					this.y = self.y + my - 3;
					super.render();
				}
			}
		};

		Dungeon.ui.add(minus);
	}

	private float speed = 0.5f;

	protected void plus() {
		zoom = Math.min(2f, zoom + speed);
		GlobalSave.put("minimap_zoom", zoom);
	}

	protected void minus() {
		zoom = Math.max(0.25f, zoom - speed);
		GlobalSave.put("minimap_zoom", zoom);
	}

	public void setSize() {
		this.w = large ? Display.GAME_WIDTH : 64;
		this.h = Math.min(this.w, Display.GAME_HEIGHT);

		this.x = Math.round(Display.GAME_WIDTH - this.w - (large ? 0 : 4));
		this.y = Math.round(Display.GAME_HEIGHT - this.h - (large ? 0 : 4));
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.wasPressed("minus")) {
			minus();
		}

		if (Input.instance.wasPressed("plus")) {
			plus();
		}

		if (!did && Input.instance.wasPressed("toggle_minimap")) {
			if (my == 0) {
				hide();
			} else {
				show();
			}
		}

		InGameState.map = large;

		if (large) {
			float s = 120f;

			if (Input.instance.isDown("left")) {
				xc += s * dt;
			}

			if (Input.instance.isDown("right")) {
				xc -= s * dt;
			}

			if (Input.instance.isDown("up")) {
				yc -= s * dt;
			}

			if (Input.instance.isDown("down")) {
				yc += s * dt;
			}

			float ix = -Input.instance.getAxis("mouseX") * s;
			float iy = Input.instance.getAxis("mouseY") * s;

			if (Math.sqrt(ix * ix + iy * iy) > 0.2) {
				xc += ix * dt;
				yc += iy * dt;
			}
		}

		if (Input.instance.wasPressed("map") && !Dungeon.game.getState().isPaused()) {
			large = !large;
			xc = 0;
			yc = 0;

			setSize();
		}
	}

	private float zoom = GlobalSave.getFloat("minimap_zoom") == 0 ? 1f : GlobalSave.getFloat("minimap_zoom");
	private Color bg = Color.valueOf("#2a2f4e");
	private Color border = Color.valueOf("#1a1932");

	@Override
	public void render() {
		if (my == 96 && !large) {
			return;
		}

		Graphics.batch.end();
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		if (large) {
			Graphics.shape.setColor(Dungeon.GRAY);
			Graphics.shape.rect(0, 0, this.w, this.h);
		} else {
			Graphics.shape.setColor(bg);
			Graphics.shape.rect(this.x + 1, this.y + this.my + 1, this.w - 2, this.h - 2);
		}

		Graphics.shape.end();

		if (!large) {
			Graphics.surface.end(Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
				Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());

			Graphics.text.begin();

			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
			Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		}

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		float px = Player.instance.x + Player.instance.w / 2f;
		float py = Player.instance.y + Player.instance.h / 2f;

		float s = (int) (large ? 6 : 4 * zoom);

		float mx = -px / (16f / s) + this.w / 2 + xc;
		float my = -py / (16f / s) + (large ? 0 : this.my) + this.h / 2 + yc;

		float o = large ? 1 : 1f * zoom;

		int xx;
		int yy;

		Graphics.shape.setColor(border);

		float cx = px - w * 2 / zoom;
		float cy = py - h * 2 / zoom;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + w * 4 / zoom) / 16) + 1);
		int fy = (int) (Math.ceil((cy + h * 4 / zoom) / 16) + 1);

		xx = Math.max(0, sx);

		for (int x = Math.max(0, sx); x < Math.min(fx, Level.getWidth()); x++) {
			yy = Math.max(0, sy);

			for (int y = Math.max(0, sy); y < Math.min(fy, Level.getHeight()); y++) {

				if (Dungeon.level.explored(x, y)) {
					Graphics.shape.rect(xx * s - o + mx, yy * s - o + my, s + o * 2, s + o * 2);
				}

				yy ++;
			}

			xx ++;
		}

		xx = Math.max(0, sx);

		for (int x = Math.max(0, sx); x < Math.min(fx, Level.getWidth()); x++) {
			yy = Math.max(0, sy);

			for (int y = Math.max(0, sy); y < Math.min(fy, Level.getHeight()); y++) {
				if (Dungeon.level.explored(x, y)) {
					int i = Level.toIndex(x, y);
					byte t = Dungeon.level.liquidData[i];

					if (t == 0) {
						t = Dungeon.level.get(i);
					}

					Graphics.shape.setColor(Terrain.getColor(t));
					Graphics.shape.rect(xx * s + mx, yy * s + my, s, s);
				}

				yy ++;
			}

			xx ++;
		}

		float plx = Player.instance.x / 16f;
		float ply = Player.instance.y / 16f;

		/*Graphics.shape.setColor(0, 0, 0, 1);
		Graphics.shape.rect(plx * s + s / 4f - o + mx, ply * s + s / 4f - o + my, s / 2f + o * 2, s / 2f + o * 2);*/
		Graphics.shape.setColor(0, 1, 0, 1);
		Graphics.shape.rect(plx * s + s / 4f + mx, ply * s + s / 4f + my, s / 2f, s / 2f);
		Graphics.shape.setColor(1, 1, 1, 1);

		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		if (!large) {
			Graphics.text.end(Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
				Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());

			Graphics.surface.begin();

			Texture texture = Graphics.text.getColorBufferTexture();
			texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

			Graphics.batch.begin();
			Graphics.batch.draw(texture, this.x + 1, this.y + 1, this.w - 2, this.h - 2,
				0, 0, (int) this.w - 2, (int) this.h - 2, false, true);
		} else {
			Graphics.batch.begin();
		}

		if (!large) {
			Graphics.render(frame, x, y + this.my);
		}
	}
}